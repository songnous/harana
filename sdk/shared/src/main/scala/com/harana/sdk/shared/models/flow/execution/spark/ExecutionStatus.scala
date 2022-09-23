package com.harana.sdk.shared.models.flow.execution.spark

import enumeratum.{CirceEnum, Enum, EnumEntry}

sealed trait ExecutionStatus extends EnumEntry
case object ExecutionStatus extends Enum[ExecutionStatus] with CirceEnum[ExecutionStatus] {
  case object None extends ExecutionStatus
  case object PendingCancellation extends ExecutionStatus
  case object PendingExecution extends ExecutionStatus
  case object Cancelled extends ExecutionStatus
  case object Executing extends ExecutionStatus
  case object Failed extends ExecutionStatus
  case object Initialised extends ExecutionStatus
  case object Killed extends ExecutionStatus
  case object Paused extends ExecutionStatus
  case object Succeeded extends ExecutionStatus
  case object TimedOut extends ExecutionStatus
  val values = findValues
}