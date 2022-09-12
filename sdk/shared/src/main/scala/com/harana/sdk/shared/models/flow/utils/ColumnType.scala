package com.harana.sdk.shared.models.flow.utils

import enumeratum.{CirceEnum, Enum, EnumEntry}

sealed trait ColumnType extends EnumEntry

case object ColumnType extends Enum[ColumnType] with CirceEnum[ColumnType] {
  case object Numeric extends ColumnType
  case object Integer extends ColumnType
  case object Float extends ColumnType
  case object Double extends ColumnType
  case object Boolean extends ColumnType
  case object String extends ColumnType
  case object Timestamp extends ColumnType
  case object Array extends ColumnType
  case object Vector extends ColumnType
  case object Other extends ColumnType
  val values = findValues
}