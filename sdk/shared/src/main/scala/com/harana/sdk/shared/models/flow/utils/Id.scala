package com.harana.sdk.shared.models.flow.utils

import com.harana.sdk.shared.utils.Random
import io.circe.generic.JsonCodec
import io.circe.{KeyDecoder, KeyEncoder}

import scala.util.Try

@JsonCodec
case class Id(value: String) extends AnyVal {
  override def toString = value
}

object Id {
  def randomId: Id = Id(Random.long)
  implicit def fromString(id: String): Id = new Id(id)

  implicit val encoder = new KeyEncoder[Id] { override def apply(key: Id) = key.toString }
  implicit val decoder = new KeyDecoder[Id] { override def apply(key: String) = Try(Id(key)).toOption }
}

trait Identifiable {
  val id: Id
}