package com.harana.sdk.shared.models.flow.exceptions

case class FlowMultiError(exceptions: Vector[FlowError]) extends FlowError {

  override val title = "Multiple errors"
  override val message = ""

}
