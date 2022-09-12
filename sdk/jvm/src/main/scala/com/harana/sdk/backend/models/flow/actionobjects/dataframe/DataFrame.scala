package com.harana.sdk.backend.models.flow.actionobjects.dataframe

import com.harana.sdk.backend.models.flow.utils.SparkTypeConverter.fromSparkStructType
import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.report.DataFrameReportGenerator
import com.harana.sdk.backend.models.flow.actions.exceptions.{BacktickInColumnNameError, DuplicatedColumnsError, WrongColumnTypeError}
import com.harana.sdk.backend.models.flow.utils.{SparkConversions, SparkTypeConverter}
import com.harana.sdk.shared.models.flow.ActionObjectInfo
import com.harana.sdk.shared.models.flow.actionobjects.descriptions
import com.harana.sdk.shared.models.flow.actionobjects.descriptions.DataFrameInferenceResult
import com.harana.sdk.shared.models.flow.actions.dataframe.DataFrameInfo
import com.harana.sdk.shared.models.flow.utils.ColumnType
import org.apache.spark.sql
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{StructField, StructType}

case class DataFrame(sparkDataFrame: sql.DataFrame = null, schema: Option[StructType] = None)
  extends DataFrameInfo with DataFrameColumnsGetter {

  def this() = this(null, None)

  schema.foreach { struct =>
    val duplicatedColumnNames = struct.fieldNames.groupBy(identity).collect { case (col, list) if list.length > 1 => col }
    if (duplicatedColumnNames.nonEmpty) throw DuplicatedColumnsError(duplicatedColumnNames.toList).toException

    val columnNamesWithBackticks = struct.fieldNames.groupBy(identity).collect { case (col, list) if col.contains("`") => col}
    if (columnNamesWithBackticks.nonEmpty) throw BacktickInColumnNameError(columnNamesWithBackticks.toList).toException
  }

  def withColumns(context: ExecutionContext, newColumns: Seq[sql.Column]) = {
    val columns = new sql.ColumnName("*") :: newColumns.toList
    val newSparkDataFrame = sparkDataFrame.select(columns: _*)
    DataFrame.fromSparkDataFrame(newSparkDataFrame)
  }

  override def report(extended: Boolean) =
    if (extended) DataFrameReportGenerator.report(sparkDataFrame) else DataFrameReportGenerator.schemaReport(sparkDataFrame)

  override def inferenceResult = schema.map(s => descriptions.DataFrameInferenceResult(fromSparkStructType(s)))
}

object DataFrame {

  def apply(sparkDataFrame: sql.DataFrame, schema: StructType): DataFrame = DataFrame(sparkDataFrame, Some(schema))

  def forInference(schema: StructType): DataFrame = forInference(Some(schema))
  def forInference(schema: Option[StructType] = None): DataFrame = DataFrame(null, schema)

  def assertExpectedColumnType(schema: StructType, expectedTypes: ColumnType*): Unit =
    for (field <- schema.fields) assertExpectedColumnType(field, expectedTypes: _*)

  def assertExpectedColumnType(column: StructField, expectedTypes: ColumnType*): Unit = {
    val actualType = SparkTypeConverter.sparkColumnTypeToColumnType(column.dataType)
    if (!expectedTypes.contains(actualType))
      throw WrongColumnTypeError(column.name, actualType, expectedTypes: _*).toException
  }

  def empty(context: ExecutionContext) = {
    val emptyRdd = context.sparkContext.parallelize(Seq[Row]())
    val emptySparkDataFrame = context.sparkSQLSession.createDataFrame(emptyRdd, StructType(Seq.empty))
    fromSparkDataFrame(emptySparkDataFrame)
  }

  def loadFromFs(context: ExecutionContext)(path: String) = {
    val dataFrame = context.sparkSQLSession.read.parquet(path)
    fromSparkDataFrame(dataFrame)
  }

  def fromSparkDataFrame(sparkDataFrame: sql.DataFrame) =
    DataFrame(sparkDataFrame, Some(sparkDataFrame.schema))
}