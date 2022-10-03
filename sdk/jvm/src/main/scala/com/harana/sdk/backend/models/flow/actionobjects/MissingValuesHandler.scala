package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.{DataFrame, DataFrameColumnsGetter}
import com.harana.sdk.backend.models.flow.actiontypes.exceptions.{MultipleTypesReplacementError, ValueConversionError}
import com.harana.sdk.backend.models.flow.utils.SparkTypeConverter
import com.harana.sdk.shared.models.flow.actionobjects.MissingValuesHandlerInfo.EmptyColumnsStrategy
import com.harana.sdk.shared.models.flow.actionobjects.MissingValuesHandlerInfo.Strategy.{RemoveColumn, RemoveRow, ReplaceWithCustomValue, ReplaceWithMode}
import com.harana.sdk.shared.models.flow.actionobjects.MissingValuesHandlerInfo
import org.apache.spark.sql
import org.apache.spark.sql.functions.{col, when}
import org.apache.spark.sql.types._

import java.sql.Timestamp
import scala.util.Try

class MissingValuesHandler extends Transformer with MissingValuesHandlerInfo {

  override def applyTransformSchema(schema: StructType) = {
    getStrategy match {
      case RemoveColumn() => None
      case _ => getMissingValueIndicator.getIndicatorPrefix match {
        case Some(prefix) =>
          val columnNames    = DataFrameColumnsGetter.getColumnNames(schema, getSelectedColumns)
          val newColumns     = columnNames.map(s => StructField(prefix + s, BooleanType, nullable = false))
          val inferredSchema = StructType(schema.fields ++ newColumns)
          Some(inferredSchema)
        case None         => Some(schema)
      }
    }
  }

  def applyTransform(context: ExecutionContext, dataFrame: DataFrame) = {
    val strategy  = getStrategy
    val columns   = dataFrame.getColumnNames(getSelectedColumns)
    val indicator = getMissingValueIndicator.getIndicatorPrefix
    val declaredAsMissingValues = $(userDefinedMissingValuesParameter).map(_.getMissingValue)
    val indicatedDf = addMissingIndicatorColumns(context, dataFrame, declaredAsMissingValues, columns, indicator)

    strategy match {
      case RemoveRow() => removeRowsWithEmptyValues(context, indicatedDf, declaredAsMissingValues, columns, indicator)
      case RemoveColumn() => removeColumnsWithEmptyValues(context, indicatedDf, declaredAsMissingValues, columns, indicator)
      case s: ReplaceWithMode => replaceWithMode(context, indicatedDf, declaredAsMissingValues, columns, s.getEmptyColumnStrategy, indicator)
      case s: ReplaceWithCustomValue => replaceWithCustomValue(context, indicatedDf, declaredAsMissingValues, columns, s.getCustomValue, indicator)
    }
  }

  private def addMissingIndicatorColumns(context: ExecutionContext, df: DataFrame, declaredAsMissingValues: Seq[String], columns: Seq[String], indicator: Option[String]) = {
    indicator match {
      case Some(prefix) =>
        val attachedColumns = columns.map(missingValueIndicatorColumn(df, declaredAsMissingValues, _, prefix))
        df.withColumns(context, attachedColumns)
      case None => df
    }
  }

  private def removeRowsWithEmptyValues(ec: ExecutionContext, df: DataFrame, declaredAsMissingValues: Seq[String], columns: Seq[String], indicator: Option[String]) = {
    val sdf = df.sparkDataFrame
    val resultDF = sdf.filter(!CommonQueries.isMissingInRowPredicate(sdf, columns, declaredAsMissingValues))
    DataFrame(resultDF, sdf.schema)
  }

  private def removeColumnsWithEmptyValues(ec: ExecutionContext, df: DataFrame, declaredAsMissingValues: Seq[String], columns: Seq[String], indicator: Option[String]) = {
    val sdf = df.sparkDataFrame

    val columnsWithMissings = columns.filter { columnName =>
      sdf.select(columnName)
        .filter(CommonQueries.isMissingInColumnPredicate(sdf, columnName, declaredAsMissingValues))
        .count() > 0
    }
    val retainedColumns = sdf.columns.filterNot(columnsWithMissings.contains)
    DataFrame.fromSparkDataFrame(sdf.select(retainedColumns.head, retainedColumns.tail.toIndexedSeq: _*))
  }

  private def replaceWithCustomValue(ec: ExecutionContext, df: DataFrame, declaredAsMissingValues: Seq[String], columns: Seq[String], customValue: String, indicator: Option[String]) = {
    val columnTypes = columns.map(c => c -> SparkTypeConverter.sparkColumnTypeToColumnType(df.schema.get(c).dataType)).toMap

    if (columnTypes.values.toSet.size != 1) throw MultipleTypesReplacementError(columnTypes).toException

    MissingValuesHandlerUtils.replaceMissings(ec, df, declaredAsMissingValues, columns, { case columnName =>
        TypeMapper.convertRawValue(df.schema.get(columnName), customValue)
      }
    )
  }

  private def replaceWithMode(ec: ExecutionContext, df: DataFrame, declaredAsMissingValues: Seq[String], columns: Seq[String], emptyColumnStrategy: EmptyColumnsStrategy, indicator: Option[String]) = {
    val columnModes = columns.map(c => c -> calculateMode(df, c, declaredAsMissingValues)).toMap

    val nonEmptyColumnModes =
      for {
        (column, modeOpt) <- columnModes
        mode <- modeOpt
      } yield (column, mode)

    val allEmptyColumns = columnModes -- nonEmptyColumnModes.keySet

    var resultDF = MissingValuesHandlerUtils.replaceMissings(ec, df, declaredAsMissingValues, columns, nonEmptyColumnModes)

    if (emptyColumnStrategy == EmptyColumnsStrategy.RemoveEmptyColumns()) {
      val retainedColumns = df.sparkDataFrame.columns.filterNot(allEmptyColumns.contains)
      resultDF = DataFrame.fromSparkDataFrame(
        resultDF.sparkDataFrame.select(retainedColumns.map(col).toIndexedSeq: _*)
      )
    }

    resultDF
  }

  private def missingValueIndicatorColumn(df: DataFrame, declaredAsMissingValues: Seq[String], column: String, prefix: String) =
    CommonQueries
      .isMissingInColumnPredicate(df.sparkDataFrame, column, declaredAsMissingValues)
      .as(prefix + column)
      .cast(BooleanType)

  private def calculateMode(df: DataFrame, column: String, declaredAsMissing: Seq[String]): Option[Any] = {
    import org.apache.spark.sql.functions.desc

    val sparkDataFrame = df.sparkDataFrame
    val sparkColumn = sparkDataFrame(column)

    val resultArray = sparkDataFrame
      .select(sparkColumn)
      .filter(!CommonQueries.isMissingInColumnPredicate(sparkDataFrame, column, declaredAsMissing))
      .groupBy(sparkColumn)
      .count()
      .orderBy(desc("count"))
      .limit(1)
      .collect()

    if (resultArray.isEmpty) None else Some(resultArray(0)(0))
  }
}

private object MissingValuesHandlerUtils {

  def replaceMissings(
      ec: ExecutionContext,
      df: DataFrame,
      declaredAsMissingValues: Seq[String],
      chosenColumns: Seq[String],
      replaceFunction: PartialFunction[String, Any]
  ) = {

    val sdf = df.sparkDataFrame

    val resultSparkDF = sdf.select(sdf.columns.toIndexedSeq.map(c => {
      if (chosenColumns.contains(c)) {
        when(
          CommonQueries.isMissingInColumnPredicate(sdf, c, declaredAsMissingValues),
          replaceFunction.applyOrElse(c, Function.const(null))
        ).otherwise(sdf(c)).as(c)
      } else
        sdf(c)
    }): _*)

    ec.dataFrameBuilder.buildDataFrame(sdf.schema, resultSparkDF.rdd)
  }
}

private object CommonQueries {

  def isMissingInRowPredicate(df: sql.DataFrame, columns: Seq[String], declaredAsMissing: Seq[String]): sql.Column = {
    val predicates = columns.map(isMissingInColumnPredicate(df, _, declaredAsMissing))
    predicates.reduce(_ or _)
  }

  def isMissingInColumnPredicate(df: sql.DataFrame, columnName: String, declaredAsMissing: Seq[String]): sql.Column = {

    val convertedMissingValues = TypeMapper.convertRawValuesToColumnTypeIfPossible(df.schema, columnName, declaredAsMissing)
    val predicate = df(columnName).isNull.or(df(columnName).isin(convertedMissingValues: _*))

    df.schema(columnName).dataType match {
      case _: DoubleType | FloatType => predicate.or(df(columnName).isNaN)
      case _  => predicate
    }
  }
}

private object TypeMapper {

  def convertRawValuesToColumnTypeIfPossible(schema: StructType, columnName: String, rawValues: Seq[String]): Seq[Any] = {
    val colIndex = schema.fieldIndex(columnName)
    val colStructField = schema.fields(colIndex)
    TypeMapper.convertRawValuesIfPossible(colStructField, rawValues)
  }

  def convertRawValuesIfPossible(field: StructField, rawValues: Seq[String]) =
    rawValues.flatMap((rawValue: String) => Try(convertRawValue(field, rawValue)).toOption)

  def convertRawValues(field: StructField, rawValues: Seq[String]) =
    rawValues.map((rawValue: String) => convertRawValue(field, rawValue))

  def convertRawValue(field: StructField, rawValue: String) =
    try {
      field.dataType match {
        case ByteType => rawValue.toByte
        case DecimalType() => new java.math.BigDecimal(rawValue)
        case DoubleType => rawValue.toDouble
        case FloatType => rawValue.toFloat
        case IntegerType => rawValue.toInt
        case LongType => rawValue.toLong
        case ShortType => rawValue.toShort
        case BooleanType => rawValue.toBoolean
        case StringType => rawValue
        case TimestampType => Timestamp.valueOf(rawValue)
      }
    } catch {
      case _: Exception => throw ValueConversionError(rawValue, field).toException
    }
}