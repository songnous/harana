package com.harana.sdk.shared.models.flow.parameters

import io.circe.generic.JsonCodec

@JsonCodec
case class BooleanParameter(name: String,
                            required: Boolean = false,
                            default: Option[Boolean] = None) extends Parameter[Boolean] {

  val parameterType = ParameterType.Boolean

  override def replicate(name: String) = copy(name = name)

}
