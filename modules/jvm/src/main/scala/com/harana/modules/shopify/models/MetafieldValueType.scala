package com.harana.modules.shopify.models

import enumeratum.values.{StringCirceEnum, StringEnum, StringEnumEntry}

sealed abstract class MetafieldValueType(val value: String) extends StringEnumEntry

object MetafieldValueType extends StringEnum[MetafieldValueType] with StringCirceEnum[MetafieldValueType] {
  case object Integer extends MetafieldValueType("integer")
  case object String extends MetafieldValueType("string")
  val values = findValues
}