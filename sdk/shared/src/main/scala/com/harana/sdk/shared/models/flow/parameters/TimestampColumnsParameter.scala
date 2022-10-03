package com.harana.sdk.shared.models.flow.parameters

import io.circe.generic.JsonCodec

@JsonCodec
case class TimestampColumnsParameter(name: String,
                                     required: Boolean = false,
                                     default: Option[Array[String]] = None) extends Parameter[Array[String]] {

  val parameterType = ParameterType.TimestampColumns

  override def replicate(name: String): TimestampColumnsParameter = copy(name = name)

}
