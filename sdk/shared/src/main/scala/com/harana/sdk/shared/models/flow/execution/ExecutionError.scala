package com.harana.sdk.shared.models.designer.flow.execution

sealed trait ExecutionError

object ExecutionError {
  case class Unknown(t: Throwable) extends ExecutionError
  case class InvalidParameter(name: String, message: String) extends ExecutionError
}