package com.harana.sdk.shared.models.flow.parameters.exceptions

case object EmptyColumnPrefixNameError$ extends ValidationError {
  val message = "Column prefix name cannot be empty"
}
