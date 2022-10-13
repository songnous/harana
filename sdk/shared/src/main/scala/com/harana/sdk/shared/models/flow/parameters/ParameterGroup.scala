package com.harana.sdk.shared.models.flow.parameters

import io.circe.generic.JsonCodec

@JsonCodec
case class ParameterGroup(name: String,
                          parameters: Parameter[_ <: Any]*)