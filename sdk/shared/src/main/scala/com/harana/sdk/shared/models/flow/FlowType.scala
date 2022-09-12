package com.harana.sdk.shared.models.flow

import enumeratum.{CirceEnum, Enum, EnumEntry}

sealed trait FlowType extends EnumEntry
object FlowType extends Enum[FlowType] with CirceEnum[FlowType] {
  case object Batch extends FlowType
  case object Streaming extends FlowType
  val values = findValues
}