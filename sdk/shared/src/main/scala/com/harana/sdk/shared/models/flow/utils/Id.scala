package com.harana.sdk.shared.models.flow.utils

import io.circe.{KeyDecoder, KeyEncoder}
import io.circe.generic.JsonCodec

import java.util.UUID
import scala.language.implicitConversions
import scala.util.Try

@JsonCodec
case class Id(value: UUID) extends AnyVal {
  override def toString = value.toString
}

object Id {
  implicit def fromUuid(uuid: UUID): Id = new Id(uuid)
  implicit def fromString(uuid: String): Id = new Id(UUID.fromString(uuid))

  def randomId: Id = fromUuid(UUID.randomUUID())

  implicit val encoder = new KeyEncoder[Id] { override def apply(key: Id) = key.toString}
  implicit val decoder = new KeyDecoder[Id] { override def apply(key: String) = Try(Id.fromString(key)).toOption }
}

trait Identifiable {
  val id: Id
}
