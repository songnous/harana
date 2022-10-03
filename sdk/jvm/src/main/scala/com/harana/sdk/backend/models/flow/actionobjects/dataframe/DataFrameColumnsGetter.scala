package com.harana.sdk.backend.models.flow.actionobjects.dataframe

import com.harana.sdk.backend.models.flow.actiontypes.exceptions.{ColumnDoesNotExistError, ColumnsDoNotExistError}
import com.harana.sdk.backend.models.flow.utils.SparkTypeConverter
import com.harana.sdk.shared.models.flow.parameters.selections._
import com.harana.sdk.shared.models.flow.utils.ColumnType
import org.apache.spark.sql.types.StructType

import java.util.UUID

trait DataFrameColumnsGetter { this: DataFrame =>
  def getColumnName(singleColumnSelection: SingleColumnSelection) = DataFrameColumnsGetter.getColumnName(sparkDataFrame.schema, singleColumnSelection)
  def getColumnNames(multipleColumnSelection: MultipleColumnSelection) = DataFrameColumnsGetter.getColumnNames(sparkDataFrame.schema, multipleColumnSelection)
}

object DataFrameColumnsGetter {

  def uniqueSuffixedColumnName(column: String) = column + "_" + UUID.randomUUID().toString

  def prefixedColumnName(column: String, prefix: String) = prefix + column

  def getColumnName(schema: StructType, singleColumnSelection: SingleColumnSelection) =
    tryGetColumnName(schema, singleColumnSelection).getOrElse(throw ColumnDoesNotExistError(singleColumnSelection, schema).toException)

  private def tryGetColumnName(schema: StructType, singleColumnSelection: SingleColumnSelection) =
    singleColumnSelection match {
      case NameSingleColumnSelection(name) => Some(name).filter(schema.fieldNames.contains)
      case IndexSingleColumnSelection(index) => if (index >= 0 && index < schema.length) Some(schema.fieldNames(index)) else None
    }

  def assertExpectedColumnType(schema: StructType, singleColumnSelection: SingleColumnSelection, expectedTypes: ColumnType*) = {
    val columnName = DataFrameColumnsGetter.getColumnName(schema, singleColumnSelection)
    DataFrame.assertExpectedColumnType(schema.fields.filter(_.name == columnName).head, expectedTypes: _*)
  }

  def getColumnNames(schema: StructType, multipleColumnSelection: MultipleColumnSelection): Seq[String] = {
    assertColumnSelectionsValid(schema, multipleColumnSelection)

    val selectedColumns = for {
      (column, index) <- schema.fields.zipWithIndex
      columnName = column.name
      columnType = SparkTypeConverter.sparkColumnTypeToColumnType(column.dataType)
      selection <- multipleColumnSelection.selections
      if DataFrameColumnsGetter.isFieldSelected(columnName, index, columnType, selection)
    } yield columnName

    val columns = if (multipleColumnSelection.excluding)
      schema.fieldNames.filterNot(selectedColumns.contains(_)).distinct
    else
      selectedColumns.distinct

    columns.toIndexedSeq
  }

  private def assertColumnSelectionsValid(schema: StructType, multipleColumnSelection: MultipleColumnSelection) = {
    val selections = multipleColumnSelection.selections
    selections.foreach(checkSelectionValidity(schema, _))
  }

  def assertColumnNamesValid(schema: StructType, columns: Seq[String]) =
    assertColumnSelectionsValid(schema, MultipleColumnSelection(List(NameColumnSelection(columns.toSet))))

  private def checkSelectionValidity(schema: StructType, selection: ColumnSelection) = {
    val valid = selection match {
      case IndexColumnSelection(indexes) =>
        val length = schema.length
        val indexesOutOfBounds = indexes.filter(index => index < 0 || index >= length)
        indexesOutOfBounds.isEmpty
      case NameColumnSelection(names) =>
        val allNames = schema.fieldNames.toSet
        val nonExistingNames = names.filter(!allNames.contains(_))
        nonExistingNames.isEmpty
      case TypeColumnSelection(_)                                        => true
      case IndexRangeColumnSelection(Some(lowerBound), Some(upperBound)) => schema.length > upperBound && lowerBound >= 0
      case IndexRangeColumnSelection(None, None)                         => true
      case IndexRangeColumnSelection(_, _)                               => throw new IllegalArgumentException("Malformed IndexRangeColumnSelection")
    }

    if (!valid) throw ColumnsDoNotExistError(selection, SparkTypeConverter.fromSparkStructType(schema)).toException
  }

  private[DataFrameColumnsGetter] def isFieldSelected(columnName: String, columnIndex: Int, columnType: ColumnType, selection: ColumnSelection) =
    selection match {
      case IndexColumnSelection(indexes)                                 => indexes.contains(columnIndex)
      case NameColumnSelection(names)                                    => names.contains(columnName)
      case TypeColumnSelection(types)                                    => types.contains(columnType)
      case IndexRangeColumnSelection(Some(lowerBound), Some(upperBound)) => columnIndex >= lowerBound && columnIndex <= upperBound
      case IndexRangeColumnSelection(None, None)                         => false
      case IndexRangeColumnSelection(_, _)                               => throw new IllegalArgumentException("Malformed IndexRangeColumnSelection")
    }
}
