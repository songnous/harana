package com.harana.sdk.shared.models.common

import io.circe.generic.JsonCodec

import java.time.Instant

@JsonCodec
case class PendingEvent(id: String,
												address: String,
												`type`: String,
												payload: String,
												created: Instant = Instant.now) extends Id with Serializable {
	type EntityType = PendingEvent
}