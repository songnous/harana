package com.harana.sdk.shared.models.schedules

import com.harana.sdk.shared.models.common.Entity.EntityId
import com.harana.sdk.shared.models.common.User.UserId
import com.harana.sdk.shared.models.common.{Background, Entity, Status, Visibility}
import com.harana.sdk.shared.models.schedules.Action.ActionId
import com.harana.sdk.shared.models.schedules.Event.EventId
import com.harana.sdk.shared.models.schedules.Notifier.NotifierId
import com.harana.sdk.shared.models.schedules.Schedule.ScheduleId
import com.harana.sdk.shared.utils.Random
import io.circe.generic.JsonCodec
import com.harana.sdk.shared.utils.CirceCodecs._

import java.time.Instant

@JsonCodec
case class Schedule(title: String,
                    description: String,
                    events: List[(EventId, Event)],
                    eventMode: EventMode,
                    actions: List[(ActionId, Action)],
                    executions: List[ScheduleExecution],
                    successNotifiers: List[NotifierId],
                    errorNotifiers: List[NotifierId],
                    createdBy: Option[UserId],
                    created: Instant,
                    updatedBy: Option[UserId],
                    updated: Instant,
                    id: ScheduleId,
                    paused: Boolean,
                    status: Status,
                    visibility: Visibility,
                    version: Long,
                    background: Option[Background],
                    tags: Set[String],
                    relationships: Map[String, EntityId])
  extends Entity with Serializable {

  type EntityType = Schedule
}

object Schedule {
  type ScheduleId = String

  def apply(title: String,
            description: String,
            events: List[(EntityId, Event)],
            eventMode: EventMode,
            actions: List[(EntityId, Action)],
            successNotifiers: List[NotifierId],
            errorNotifiers: List[NotifierId],
            createdBy: Option[UserId],
            visibility: Visibility,
            background: Option[Background],
            tags: Set[String]): Schedule = {
    apply(title, description, events, eventMode, actions, List(), successNotifiers, errorNotifiers, createdBy, Instant.now, createdBy, Instant.now, Random.long, false, Status.Active, visibility, 1L, background, tags, Map())
  }
}