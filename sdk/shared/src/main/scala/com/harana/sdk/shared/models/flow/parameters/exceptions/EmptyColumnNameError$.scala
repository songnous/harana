package com.harana.sdk.shared.models.flow.parameters.exceptions

case object EmptyColumnNameError$ extends ValidationError {
  val message = "Column name cannot be empty."
}