package com.harana.sdk.shared.models.common

import enumeratum.{CirceEnum, Enum, EnumEntry}

sealed trait DateTimeStyle extends EnumEntry
case object DateTimeStyle extends Enum[DateTimeStyle] with CirceEnum[DateTimeStyle] {
  case object None extends DateTimeStyle
  case object Short extends DateTimeStyle
  case object Medium extends DateTimeStyle
  case object Long extends DateTimeStyle
  case object Full extends DateTimeStyle
  val values = findValues
}