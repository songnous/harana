package com.harana.sdk.backend.models.flow.actiontypes.exceptions

import com.harana.sdk.shared.models.flow.exceptions.FlowError
import io.circe.generic.JsonCodec

@JsonCodec
case class UnknownActionExecutionError() extends FlowError {
  val message = "The action is unknown and can't be executed"
}
