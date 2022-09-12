package com.harana.sdk.shared.models.flow.parameters.exceptions

import com.harana.sdk.shared.models.flow.exceptions.FlowError
import io.circe.generic.JsonCodec

@JsonCodec
case class NoArgumentConstructorRequiredError(className: String) extends FlowError {
  val message = className
}