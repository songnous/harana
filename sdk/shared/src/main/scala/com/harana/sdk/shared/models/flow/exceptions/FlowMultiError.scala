package com.harana.sdk.shared.models.flow.exceptions

case class FlowMultiError(exceptions: List[FlowError]) extends FlowError {

  override val title = "Multiple errors"
  override val message = ""

}
