package com.harana.sdk.shared.models.common

import java.time.Instant
import com.harana.sdk.shared.models.common.Entity.EntityId
import com.harana.sdk.shared.models.common.User.UserId
import com.harana.sdk.shared.utils.CirceCodecs._
import enumeratum._

import scala.annotation.meta.field

sealed trait Status extends EnumEntry
case object Status extends Enum[Status] with CirceEnum[Status] {
  case object Active extends Status
  case object Paused extends Status
  case object Deleted extends Status
  val values = findValues
}

trait Entity extends Id {
  type EntityType <: Entity

  val createdBy: Option[UserId]
  val created: Instant
  val updated: Instant
  val updatedBy: Option[UserId]
  val id: EntityId
  val status: Status
  val version: Long
  val tags: Set[String]
  val relationships: Map[String, EntityId]
}

object Entity {
  type EntityId = String
}