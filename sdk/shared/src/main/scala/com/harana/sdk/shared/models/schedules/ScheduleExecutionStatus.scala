package com.harana.sdk.shared.models.schedules

import enumeratum.{CirceEnum, Enum, EnumEntry}

sealed trait ScheduleExecutionStatus extends EnumEntry
case object ScheduleExecutionStatus extends Enum[ScheduleExecutionStatus] with CirceEnum[ScheduleExecutionStatus] {
  case object None extends ScheduleExecutionStatus
  case object PendingCancellation extends ScheduleExecutionStatus
  case object PendingExecution extends ScheduleExecutionStatus
  case object Cancelled extends ScheduleExecutionStatus
  case object Executing extends ScheduleExecutionStatus
  case object Failed extends ScheduleExecutionStatus
  case object Initialised extends ScheduleExecutionStatus
  case object Killed extends ScheduleExecutionStatus
  case object Paused extends ScheduleExecutionStatus
  case object Succeeded extends ScheduleExecutionStatus
  case object TimedOut extends ScheduleExecutionStatus
  val values = findValues
}