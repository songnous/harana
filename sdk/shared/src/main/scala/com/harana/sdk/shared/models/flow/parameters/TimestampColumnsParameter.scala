package com.harana.sdk.shared.models.flow.parameters

import io.circe.generic.JsonCodec

@JsonCodec
case class TimestampColumnsParameter(name: String,
                                     tags: List[String] = List(),
                                     required: Boolean = false) extends Parameter[List[String]] {

  val parameterType = ParameterType.TimestampColumns

  override def replicate(name: String): TimestampColumnsParameter = copy(name = name)

}
