package com.harana.sdk.backend.models.flow.actions.exceptions

import com.harana.sdk.shared.models.flow.exceptions.ActionExecutionError
import com.harana.sdk.shared.models.flow.utils.ColumnType

case class WrongColumnTypeError(override val message: String) extends ActionExecutionError

object WrongColumnTypeError {

  def apply(columnName: String, actualType: ColumnType, expectedTypes: ColumnType*): WrongColumnTypeError =
    WrongColumnTypeError(
      s"Column '$columnName' has type '$actualType' instead of " +
        s"expected ${expectedTypes.map(t => s"'${t.toString}'").mkString(" or ")}."
    )

}
