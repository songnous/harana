package com.harana.sdk.shared.models.flow.parameters.exceptions

case class ParametersEqualError(firstParameterName: String, secondParameterName: String, value: String) extends ValidationError {
  val message = s"'$firstParameterName' is equal to '$secondParameterName' (both are equal to '$value')."
}