package com.harana.sdk.shared.models.flow.parameters

case class ParameterGroup(name: String,
                          parameters: List[Parameter[_]])