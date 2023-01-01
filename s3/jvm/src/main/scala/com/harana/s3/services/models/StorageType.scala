package com.harana.s3.services.models

import enumeratum._

sealed trait StorageType extends EnumEntry

case object StorageType extends Enum[StorageType] {
  case object CONTAINER extends StorageType
  case object BLOB extends StorageType
  case object FOLDER extends StorageType
  case object RELATIVE_PATH extends StorageType
  val values = findValues
}