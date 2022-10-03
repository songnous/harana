package com.harana.sdk.backend.models.flow.actiontypes.exceptions

import com.harana.sdk.shared.models.flow.exceptions.FlowError

case class SqlExpressionError(formula: String, errorText: String) extends FlowError {
  val message = s"SQL formula '$formula' cannot be evaluated ($errorText)"
}
