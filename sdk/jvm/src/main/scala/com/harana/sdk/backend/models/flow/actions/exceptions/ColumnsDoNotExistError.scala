package com.harana.sdk.backend.models.flow.actions.exceptions

import ColumnsDoNotExistError._
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.shared.models.flow.StructType
import com.harana.sdk.shared.models.flow.exceptions.ActionExecutionError
import com.harana.sdk.shared.models.flow.parameters.selections.{ColumnSelection, IndexColumnSelection, IndexRangeColumnSelection, NameColumnSelection, TypeColumnSelection}
import io.circe.generic.JsonCodec

@JsonCodec
case class ColumnsDoNotExistError(invalidSelection: ColumnSelection, schema: StructType) extends ActionExecutionError {
  val message = exceptionMessage(invalidSelection, schema)
}

object ColumnsDoNotExistError {

  private def exceptionMessage(selection: ColumnSelection, schema: StructType) =
    s"${selectionDescription(selection, schema)} (${schemaDescription(selection, schema)})"

  private def selectionDescription(selection: ColumnSelection, schema: StructType) =
    selection match {
      case IndexColumnSelection(indices) =>
        s"One or more columns from index list: (${indices.mkString(", ")}) does not exist in the input DataFrame"
      case IndexRangeColumnSelection(begin, end) =>
        s"One or more columns from index range ${begin.get}..${end.get} does not exist in the input DataFrame"
      case NameColumnSelection(names)            =>
        val dfColumnNames                                      = schema.map(field => field.name)
        val missingColumns                                     = (names -- dfColumnNames.toSet).map(name => s"`$name`")
        val (pluralityDependentPrefix, pluralityDependentVerb) =
          if (missingColumns.size > 1) ("Columns:", "do") else ("Column", "does")
        s"$pluralityDependentPrefix ${missingColumns.mkString(", ")}" +
          s" $pluralityDependentVerb not exist in the input DataFrame"
      case TypeColumnSelection(_)                =>
        throw new IllegalStateException("This shouldn't be called on TypeColumnSelection!")
    }

  private def schemaDescription(selection: ColumnSelection, schema: StructType) = {
    selection match {
      case IndexColumnSelection(_) | IndexRangeColumnSelection(_, _) =>
        s"index range: 0..${schema.length - 1}"
      case NameColumnSelection(names)                                =>
        s"column names: ${schema.fields.map(field => s"`${field.name}`").mkString(", ")}"
      case TypeColumnSelection(_)                                    =>
        throw new IllegalStateException("This shouldn't be called on TypeColumnSelection!")
    }
  }
}
