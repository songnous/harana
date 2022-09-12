package com.harana.sdk.shared.models.flow.parameters

import enumeratum._

sealed trait StorageType extends EnumEntry

case object StorageType extends Enum[StorageType] {
  case object File extends StorageType
  case object Jdbc extends StorageType
  case object GoogleSheet extends StorageType
  val values = findValues
}