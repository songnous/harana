package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.flow.{ActionType2To1, DataFrame2To1Action, ExecutionContext}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.{DataFrame, DataFrameColumnsGetter}
import com.harana.sdk.backend.models.flow.actions.exceptions.ColumnsDoNotExistError
import com.harana.sdk.backend.models.flow.utils.SparkTypeConverter
import com.harana.sdk.shared.models.flow.actions.JoinInfo.ColumnPair
import com.harana.sdk.shared.models.flow.actions.JoinInfo
import com.harana.sdk.shared.models.flow.parameters.selections.{NameColumnSelection, SingleColumnSelection}
import org.apache.spark.sql
import org.apache.spark.sql.Column
import org.apache.spark.sql.types.StructType

import scala.reflect.runtime.universe.TypeTag

class Join extends ActionType2To1[DataFrame, DataFrame, DataFrame]
  with DataFrame2To1Action
  with JoinInfo {

  def execute(ldf: DataFrame, rdf: DataFrame)(context: ExecutionContext) = {
    println("Execution of " + this.getClass.getSimpleName + " starts")

    validateSchemas(ldf.sparkDataFrame.schema, rdf.sparkDataFrame.schema)
    val joinType = getJoinType
    val columnNames= RenamedColumnNames(ldf.sparkDataFrame.columns, rdf.sparkDataFrame.columns)
    val leftColumnNames = getSelectedJoinColumnsNames(ldf.sparkDataFrame.schema, _.getLeftColumn)
    val rightColumnNames = getSelectedJoinColumnsNames(rdf.sparkDataFrame.schema, _.getRightColumn)
    val joinColumnsPairs = leftColumnNames.zip(rightColumnNames)

    val lsdf = sparkDFWithColumnsRenamed(ldf.sparkDataFrame, columnNames.left.original, columnNames.left.renamed)
    val rsdf = sparkDFWithColumnsRenamed(rdf.sparkDataFrame, columnNames.right.original, columnNames.right.renamed)

    val renamedJoinColumnsPairs = joinColumnsPairs.map { case (leftColumn, rightColumn) =>
      val columnLeft  = lsdf(columnNames.left.originalToRenamed(leftColumn))
      val columnRight = rsdf(columnNames.right.originalToRenamed(rightColumn))
      (columnLeft, columnRight)
    }

    println("Prepare joining condition")
    val joinCondition = prepareJoiningCondition(renamedJoinColumnsPairs)

    println(s"$joinType Join of two DataFrames")
    val joinedDataFrame = lsdf.join(rsdf, joinCondition, joinType.toSpark)

    println("Removing additional columns in right DataFrame")
    val noDuplicatesSparkDF = sparkDFWithRemovedDuplicatedColumns(joinedDataFrame, renamedJoinColumnsPairs, getColumns(lsdf), getColumns(rsdf))

    DataFrame.fromSparkDataFrame(noDuplicatesSparkDF)
  }

  private def sparkDFWithColumnsRenamed(initSdf: sql.DataFrame, colFrom: Seq[String], colTo: Seq[String]) =
    colFrom.zip(colTo).foldLeft(initSdf) { (sdf, pair) =>
      val (from, to) = pair
      sdf.withColumnRenamed(from, to)
    }

  private def sparkDFWithRemovedDuplicatedColumns(joinedDataFrame: sql.DataFrame, joinColumnsPairs: Seq[(Column, Column)], leftColumns: Seq[Column], rightColumns: Seq[Column]) = {
    val (_, rightJoinColumns) = joinColumnsPairs.unzip
    val columns = leftColumns ++ rightColumns.filter(col => !rightJoinColumns.contains(col))
    assert(columns.nonEmpty)
    joinedDataFrame.select(columns: _*)
  }

  private def getSelectedJoinColumnsNames(schema: StructType, selector: ColumnPair => SingleColumnSelection) =
    getJoinColumns.map(columnPair => DataFrameColumnsGetter.getColumnName(schema, selector(columnPair)))

  private def validateSchemas(leftSchema: StructType, rightSchema: StructType) = {
    val leftJoinColumnNames  = getSelectedJoinColumnsNames(leftSchema, _.getLeftColumn)
    val rightJoinColumnNames = getSelectedJoinColumnsNames(rightSchema, _.getRightColumn)

    println("Validate that columns used for joining is not empty")
    if (leftJoinColumnNames.isEmpty) throw ColumnsDoNotExistError(NameColumnSelection(leftJoinColumnNames.toSet), SparkTypeConverter.fromSparkStructType(leftSchema)).toException
    if (rightJoinColumnNames.isEmpty) throw ColumnsDoNotExistError(NameColumnSelection(rightJoinColumnNames.toSet), SparkTypeConverter.fromSparkStructType(rightSchema)).toException

    println("Validate types of columns used to join two DataFrames")
    leftJoinColumnNames.zip(rightJoinColumnNames).foreach { case (leftCol, rightCol) =>
      DataFrame.assertExpectedColumnType(leftSchema.apply(leftCol), SparkTypeConverter.sparkColumnTypeToColumnType(rightSchema.apply(rightCol).dataType))
    }
  }

  private def prepareJoiningCondition(joinColumns: Seq[(Column, Column)]) = {
    require(joinColumns.nonEmpty)
    val initialCondition = joinColumns.head match {
      case (leftHead, rightHead) => leftHead === rightHead
    }
    joinColumns.foldLeft(initialCondition) { case (acc, (leftColumn, rightColumn)) => acc && (leftColumn === rightColumn)}
  }

  private def getColumns(sdf: sql.DataFrame) =
    sdf.columns.map(sdf(_)).toIndexedSeq

  lazy val tTagTO_0: TypeTag[DataFrame] = typeTag
}
