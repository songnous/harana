package com.harana.sdk.shared.models.flow.parameters.exceptions

import io.circe.generic.JsonCodec

@JsonCodec
case class TypeConversionError(className: String, targetTypeName: String) extends ValidationError {
  val message = s"Cannot convert $className to $targetTypeName."
}