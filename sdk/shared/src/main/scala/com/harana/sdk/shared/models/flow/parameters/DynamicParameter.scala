package com.harana.sdk.shared.models.flow.parameters

import io.circe.Json
import io.circe.generic.JsonCodec

@JsonCodec
case class DynamicParameter(name: String,
                            required: Boolean = false,
                            default: Option[Json] = None,
                            inputPort: Int) extends Parameter[Json] {

  val parameterType: ParameterType = ParameterType.Dynamic

  override def replicate(name: String): DynamicParameter = new DynamicParameter(name, required, default, inputPort)

}