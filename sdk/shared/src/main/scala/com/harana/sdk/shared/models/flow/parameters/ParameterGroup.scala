package com.harana.sdk.shared.models.flow.parameters

import io.circe.generic.JsonCodec

@JsonCodec
case class ParameterGroup(name: Option[String],
                          parameters: Parameter[_ <: Any]*)