package com.harana.sdk.shared.models.flow.parameters.validators

import com.harana.sdk.shared.models.flow.exceptions.FlowError
import com.harana.sdk.shared.models.flow.parameters.exceptions.MatchError
import io.circe.syntax.EncoderOps

import scala.util.matching.Regex

case class RegexValidator(regex: Regex) extends RegexValidatorLike
class AcceptAllRegexValidator() extends RegexValidator(".*".r)
class SingleCharRegexValidator() extends RegexValidator(".".r)

trait RegexValidatorLike extends Validator[String] {

  val regex: Regex
  val validatorType = ValidatorType.Regex

  def configurationToJson = ("regex" -> regex.toString()).asJson

  def validate(name: String, parameter: String): Vector[FlowError] =
    if (parameter.matches(regex.toString)) Vector.empty else Vector(MatchError(parameter, regex))
}

