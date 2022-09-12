package com.harana.sdk.backend.models.flow.actions.exceptions

import com.harana.sdk.shared.models.flow.exceptions.ActionExecutionError
import com.harana.sdk.shared.models.flow.utils.ColumnType

case class MultipleTypesReplacementError(columnTypes: Map[String, ColumnType]) extends ActionExecutionError {
  val message = "Missing value replacement is impossible - selected columns: " +
    s"${columnTypes.keys.mkString(", ")} have different column types: " +
    s"${columnTypes.keys.map(columnTypes(_)).mkString(", ")}"
}
