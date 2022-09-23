package com.harana.sdk.shared.models.flow.execution.spark

sealed trait ExecutionError

object ExecutionError {
  case class Unknown(t: Throwable) extends ExecutionError
  case class InvalidParameter(name: String, message: String) extends ExecutionError
}