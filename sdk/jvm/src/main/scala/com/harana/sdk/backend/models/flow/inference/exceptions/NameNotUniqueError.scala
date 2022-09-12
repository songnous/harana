package com.harana.sdk.backend.models.flow.inference.exceptions

import com.harana.sdk.shared.models.flow.exceptions.FlowError
import io.circe.generic.JsonCodec

@JsonCodec
case class NameNotUniqueError(name: String) extends FlowError {
  val message = s"Name '$name' is not unique"
}