package com.harana.sdk.shared.models.flow

import enumeratum._

sealed trait PortPosition extends EnumEntry
case object PortPosition extends Enum[PortPosition] with CirceEnum[PortPosition] {
  case object Left extends PortPosition
  case object Center extends PortPosition
  case object Right extends PortPosition
  val values = findValues

  implicit def ordering = new Ordering[PortPosition] {
    def compare(x: PortPosition, y: PortPosition) =
      (x, y) match {
        case (Left, _) => -1
        case (Center, Left) => 0
        case (Center, Right) => -1
        case (Right, _) => 1
        case _ => 0
      }
  }
}

sealed trait Gravity extends EnumEntry
case object Gravity extends Enum[Gravity] with CirceEnum[Gravity] {
  case object GravitateLeft extends Gravity
  case object GravitateRight extends Gravity
  val values = findValues
}
