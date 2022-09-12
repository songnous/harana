package com.harana.sdk.shared.models.flow.parameters.exceptions

import io.circe.generic.JsonCodec

@JsonCodec
case class ArrayTooLong(name: String, arrayLength: Int, maxLength: Int) extends ValidationError {
  val message = s"Array '$name' is too long. Length of `$name` is `$arrayLength` but needs to be at most `$maxLength`."
}