package com.harana.sdk.shared.models.flow.exceptions

import io.circe.generic.JsonCodec

@JsonCodec
case class IllegalHaranaArgumentError(message: String) extends HaranaError {
  val code = FailureCode.IllegalArgumentException
  val title = "Illegal Harana argument exception"
}