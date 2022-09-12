package com.harana.sdk.shared.models.flow.parameters.validators

import enumeratum._

sealed trait ValidatorType extends EnumEntry
case object ValidatorType extends Enum[ValidatorType] with CirceEnum[ValidatorType] {
  case object Range extends ValidatorType
  case object Regex extends ValidatorType
  case object ArrayLength extends ValidatorType
  case object ArrayComplex extends ValidatorType

  val values = findValues
}
