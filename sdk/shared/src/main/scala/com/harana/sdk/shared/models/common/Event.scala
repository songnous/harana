package com.harana.sdk.shared.models.common

import java.time.Instant

import com.harana.sdk.shared.models.common.Entity.EntityId
import com.harana.sdk.shared.models.common.Event.EventId
import com.harana.sdk.shared.models.common.User.UserId
import io.circe.generic.JsonCodec
import com.harana.sdk.shared.utils.CirceCodecs._
import com.harana.sdk.shared.utils.Random

@JsonCodec
case class Event(eventType: String,
								 parameters: Map[String, String],
                 createdBy: Option[UserId],
                 created: Instant,
                 updatedBy: Option[UserId],
                 updated: Instant,
								 id: EventId,
                 status: Status,
								 visibility: Visibility,
								 version: Long,
								 tags: Set[String],
                 relationships: Map[String, EntityId])
	extends Entity with Serializable {

	type EntityType = Event
}

object Event {
	type EventId = String

	def apply(eventType: String, parameters: Map[String, String], createdBy: UserId): Event = {
		apply(eventType, parameters, Some(createdBy), Instant.now, Some(createdBy), Instant.now, Random.long, Status.Active, Visibility.Owner, 1L, Set(), Map())
	}
}