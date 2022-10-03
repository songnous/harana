package com.harana.sdk.backend.models.flow.actiontypes.exceptions

import com.harana.sdk.shared.models.flow.exceptions.ActionExecutionError

case class CustomActionExecutionError(error: String) extends ActionExecutionError {
  val message = s"Custom action execution failed: $error"
}
