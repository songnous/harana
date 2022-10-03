package com.harana.sdk.shared.models.flow.parameters

case class ParameterGroup(name: Option[String],
                          parameters: Parameter[_]*)