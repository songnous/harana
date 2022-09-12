package com.harana.sdk.shared.models.flow

sealed trait ExecutionMode

object ExecutionMode {
  case object Batch extends ExecutionMode
  case object Interactive extends ExecutionMode
}
