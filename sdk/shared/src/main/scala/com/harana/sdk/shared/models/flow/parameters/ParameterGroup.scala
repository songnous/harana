package com.harana.sdk.shared.models.flow.parameters

import io.circe.{Decoder, Encoder}
import io.circe.generic.JsonCodec

@JsonCodec
case class ParameterGroup(name: Option[String],
                          parameters: Parameter[_]*)

object ParameterGroup {
  implicit val decodeParameter: Decoder[Parameter[_]] = Decoder.decodeString.emap { str => Left("") }
  implicit val encodeParameter: Encoder[Parameter[_]] = Encoder.encodeString.contramap[Parameter[_]](_.toString)

}