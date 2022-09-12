package com.harana.sdk.shared.models.flow.parameters.exceptions

import io.circe.generic.JsonCodec

@JsonCodec
case class ArrayTooShort(name: String, arrayLength: Int, minLength: Int) extends ValidationError {
  val message = s"Array '$name' is too short. Length of `$name` is `$arrayLength` but needs to be at least `$minLength`."
}