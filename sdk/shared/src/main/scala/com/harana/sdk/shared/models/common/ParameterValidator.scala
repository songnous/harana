package com.harana.sdk.shared.models.common

import enumeratum._

sealed trait ParameterValidator extends EnumEntry
case object ParameterValidator extends Enum[ParameterValidator] with CirceEnum[ParameterValidator] {
  case object MinimumCharactersValidator extends ParameterValidator with Serializable
  case class RegexPattern(pattern: String) extends ParameterValidator with Serializable
  val values = findValues
}
