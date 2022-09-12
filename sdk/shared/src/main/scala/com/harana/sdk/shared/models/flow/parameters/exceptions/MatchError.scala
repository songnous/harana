package com.harana.sdk.shared.models.flow.parameters.exceptions

import scala.util.matching.Regex

case class MatchError(value: String, regex: Regex) extends ValidationError {
  val message = s"Parameter value `$value` does not match regex `$regex`."
}