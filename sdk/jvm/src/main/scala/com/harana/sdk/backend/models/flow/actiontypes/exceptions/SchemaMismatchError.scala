package com.harana.sdk.backend.models.flow.actiontypes.exceptions

import com.harana.sdk.shared.models.flow.exceptions.ActionExecutionError

case class SchemaMismatchError(error: String) extends ActionExecutionError {
  val message = s"Schema mismatch: $error"
}
