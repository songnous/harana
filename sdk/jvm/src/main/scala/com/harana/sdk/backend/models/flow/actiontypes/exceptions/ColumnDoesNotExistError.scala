package com.harana.sdk.backend.models.flow.actiontypes.exceptions

import ColumnDoesNotExistError._
import com.harana.sdk.shared.models.flow.exceptions.ActionExecutionError
import com.harana.sdk.shared.models.flow.parameters.selections.{IndexSingleColumnSelection, NameSingleColumnSelection, SingleColumnSelection}
import org.apache.spark.sql.types.StructType

case class ColumnDoesNotExistError(selection: SingleColumnSelection, schema: StructType) extends ActionExecutionError {
  val message = exceptionMessage(selection, schema)
}

object ColumnDoesNotExistError {

  private def exceptionMessage(selection: SingleColumnSelection, schema: StructType) =
    s"Column ${selectionDescription(selection)} " +
      s"does not exist in the input DataFrame (${schemaDescription(selection, schema)})"

  private def selectionDescription(selection: SingleColumnSelection) =
    selection match {
      case NameSingleColumnSelection(name)   => s"`$name`"
      case IndexSingleColumnSelection(index) => s"with index $index"
    }

  private def schemaDescription(selection: SingleColumnSelection, schema: StructType) = {
    selection match {
      case IndexSingleColumnSelection(_) =>
        s"index range: 0..${schema.length - 1}"
      case NameSingleColumnSelection(_)  =>
        s"column names: ${schema.fields.map(field => s"`${field.name}`").mkString(", ")}"
    }
  }
}
