package com.harana.sdk.shared.models.schedules

import com.harana.sdk.shared.models.common.Entity.EntityId
import com.harana.sdk.shared.models.common.User.UserId
import com.harana.sdk.shared.models.common.{Entity, Status, Visibility}
import com.harana.sdk.shared.models.schedules.Schedule.ScheduleId
import com.harana.sdk.shared.models.schedules.ScheduleExecution.ScheduleExecutionId
import com.harana.sdk.shared.utils.Random
import enumeratum.{CirceEnum, Enum, EnumEntry}
import io.circe.generic.JsonCodec

import java.time.Instant

@JsonCodec
case class ScheduleExecution(scheduleId: ScheduleId,
                             started: Instant,
                             finished: Option[Instant],
                             executionStatus: ScheduleExecutionStatus,
                             createdBy: Option[UserId],
                             created: Instant,
                             updatedBy: Option[UserId],
                             updated: Instant,
                             id: ScheduleExecutionId,
                             status: Status,
                             visibility: Visibility,
                             version: Long,
                             tags: Set[String],
                             relationships: Map[String, EntityId])
  extends Entity with Serializable {

  type EntityType = ScheduleExecution
}

object ScheduleExecution {
  type ScheduleExecutionId = String

  def apply(scheduleId: ScheduleId,
            createdBy: Option[UserId],
            visibility: Visibility,
            tags: Set[String]): ScheduleExecution =
    apply(scheduleId, Instant.now, None, ScheduleExecutionStatus.None, createdBy, Instant.now, createdBy, Instant.now, Random.long, Status.Active, visibility, 1L, tags, Map())
}

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