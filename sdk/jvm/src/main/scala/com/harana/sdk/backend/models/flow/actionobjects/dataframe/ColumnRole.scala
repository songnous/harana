package com.harana.sdk.backend.models.flow.actionobjects.dataframe

import enumeratum.values._

sealed abstract class RichValue(val value: String) extends StringEnumEntry
case object RichValue extends StringEnum[RichValue] with StringCirceEnum[RichValue] {
  case object Feature extends RichValue("feature")
  case object Label extends RichValue("label")
  case object Prediction extends RichValue("prediction")
  case object Id extends RichValue("id")
  case object Ignored extends RichValue("ignored")
  val values = findValues
}