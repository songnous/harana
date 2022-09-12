package com.harana.sdk.shared.models.flow.parameters.exceptions

case class ParamValueNotProvidedError(name: String) extends ValidationError {
  val message = s"No value for parameter '$name'"
}