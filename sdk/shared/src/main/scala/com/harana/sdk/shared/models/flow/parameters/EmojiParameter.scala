package com.harana.sdk.shared.models.flow.parameters

import io.circe.generic.JsonCodec

@JsonCodec
case class EmojiParameter(name: String,
                          required: Boolean = false,
                          default: Option[String] = None) extends Parameter[String] {

  val parameterType = ParameterType.Emoji

  override def replicate(name: String) = copy(name = name)
}