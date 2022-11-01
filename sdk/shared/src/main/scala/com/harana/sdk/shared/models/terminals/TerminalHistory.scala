package com.harana.sdk.shared.models.terminals

import io.circe.generic.JsonCodec
import enumeratum._

sealed trait HistoryType extends EnumEntry
case object HistoryType extends Enum[HistoryType] with CirceEnum[HistoryType] {
  case object Stdin extends HistoryType
  case object Stdout extends HistoryType
  case object Stderr extends HistoryType
  val values = findValues
}

@JsonCodec
case class TerminalHistory(historyType: HistoryType, message: String)