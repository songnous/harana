package com.harana.sdk.shared.models.flow.parameters.validators

import com.harana.sdk.shared.models.flow.exceptions.FlowError
import com.harana.sdk.shared.models.flow.parameters.exceptions.MatchError
import io.circe.generic.JsonCodec

import scala.util.matching.Regex

trait RegexValidator extends Validator[String] {
  val regex: Regex

  def validate(name: String, parameter: String): List[FlowError] =
    if (parameter.matches(regex.toString)) List.empty else List(MatchError(parameter, regex))
}

object RegexValidator {

  object AcceptAll extends RegexValidator {
    val regex = ".*".r
  }

  object Email extends RegexValidator {
    val regex = "/^\\S+@\\S+\\.\\S+$/".r
  }

  object SingleChar extends RegexValidator {
    val regex = ".".r
  }

  object URI extends RegexValidator {
    val regex = "https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&//=]*)".r
  }
}