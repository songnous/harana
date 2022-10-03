package com.harana.sdk.backend.models.flow.actiontypes.exceptions

import com.harana.sdk.shared.models.flow.exceptions.FlowError

case object EmptyDataframeError$ extends FlowError {
  val message = "DataFrame cannot be empty."
}
