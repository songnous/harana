package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.common.Parameter.Color
import io.circe.generic.JsonCodec

@JsonCodec
case class ColorParameter(name: String,
                          required: Boolean = false,
                          default: Option[Color] = None) extends Parameter[Color] {

  val parameterType = ParameterType.Color

  override def replicate(name: String) = copy(name = name)
}