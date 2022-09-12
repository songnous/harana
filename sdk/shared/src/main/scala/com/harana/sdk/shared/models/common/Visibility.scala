package com.harana.sdk.shared.models.common

import enumeratum.{CirceEnum, Enum, EnumEntry}

sealed trait Visibility extends EnumEntry
case object Visibility extends Enum[Visibility] with CirceEnum[Visibility] {
  case object Owner extends Visibility
  case object Group extends Visibility
  case object Public extends Visibility
  val values = findValues
}