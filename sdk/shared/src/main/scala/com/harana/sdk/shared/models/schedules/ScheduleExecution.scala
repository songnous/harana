package com.harana.sdk.shared.models.schedules

import com.harana.sdk.shared.models.common.Entity.EntityId
import com.harana.sdk.shared.models.schedules.Action.ActionId
import com.harana.sdk.shared.models.schedules.Schedule.ScheduleId
import enumeratum.{CirceEnum, Enum, EnumEntry}
import io.circe.generic.JsonCodec

import java.time.Instant

@JsonCodec
case class ScheduleExecution(id: EntityId,
                             scheduleId: ScheduleId,
                             started: Instant,
                             finished: Option[Instant] = None,
                             status: ScheduleExecutionStatus = ScheduleExecutionStatus.None,
                             actionExecutions: Map[ActionId, ScheduleActionExecution] = Map(),
                             eventExecutions: Map[ActionId, ScheduleEventExecution] = Map())

@JsonCodec
case class ScheduleActionExecution(started: Instant,
                                   finished: Option[Instant] = None,
                                   status: ScheduleActionExecutionStatus)

@JsonCodec
case class ScheduleEventExecution(triggered: Instant,
                                  status: ScheduleEventExecutionStatus)


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

sealed trait ScheduleActionExecutionStatus extends EnumEntry
case object ScheduleActionExecutionStatus extends Enum[ScheduleActionExecutionStatus] with CirceEnum[ScheduleActionExecutionStatus] {
  case object None extends ScheduleActionExecutionStatus
  case object Cancelled extends ScheduleActionExecutionStatus
  case object Executing extends ScheduleActionExecutionStatus
  case object Failed extends ScheduleActionExecutionStatus
  case object Initialised extends ScheduleActionExecutionStatus
  case object Killed extends ScheduleActionExecutionStatus
  case object Paused extends ScheduleActionExecutionStatus
  case object Succeeded extends ScheduleActionExecutionStatus
  case object TimedOut extends ScheduleActionExecutionStatus
  val values = findValues
}

sealed trait ScheduleEventExecutionStatus extends EnumEntry
case object ScheduleEventExecutionStatus extends Enum[ScheduleEventExecutionStatus] with CirceEnum[ScheduleEventExecutionStatus] {
  case object None extends ScheduleEventExecutionStatus
  case object Triggered extends ScheduleEventExecutionStatus
  val values = findValues
}