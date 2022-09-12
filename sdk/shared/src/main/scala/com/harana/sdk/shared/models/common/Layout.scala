package com.harana.sdk.shared.models.common

import com.harana.sdk.shared.models.catalog.Panel.PanelSlotId
import com.harana.sdk.shared.models.catalog.UserPanelType.UserPanelTypeId
import com.harana.sdk.shared.plugin.PanelType.PanelTypeId
import enumeratum._
import io.circe.generic.JsonCodec
import com.harana.sdk.shared.utils.CirceCodecs._

@JsonCodec
case class Layout(rows: List[Row])

@JsonCodec
case class Row(columns: List[Column])

@JsonCodec
case class Column(contents: List[PanelSlot],
                  width: ColumnWidth,
                  offset: ColumnOffset = ColumnOffset.None)

@JsonCodec
case class PanelSlot(panelSlotId: PanelSlotId,
                     panelType: Either[PanelTypeId, UserPanelTypeId],
                     minimumHeight: Int)

sealed trait ColumnOffset extends EnumEntry
case object ColumnOffset extends Enum[ColumnOffset] with CirceEnum[ColumnOffset] {
  case object None extends ColumnOffset
  case object One extends ColumnOffset
  case object Two extends ColumnOffset
  case object Three extends ColumnOffset
  case object Four extends ColumnOffset
  case object Five extends ColumnOffset
  case object Six extends ColumnOffset
  case object Seven extends ColumnOffset
  case object Eight extends ColumnOffset
  case object Nine extends ColumnOffset
  case object Ten extends ColumnOffset
  case object Eleven extends ColumnOffset
  case object Twelve extends ColumnOffset
  val values = findValues
}

sealed trait ColumnWidth extends EnumEntry
case object ColumnWidth extends Enum[ColumnWidth] with CirceEnum[ColumnWidth] {
  case object One extends ColumnWidth
  case object Two extends ColumnWidth
  case object Three extends ColumnWidth
  case object Four extends ColumnWidth
  case object Five extends ColumnWidth
  case object Six extends ColumnWidth
  case object Seven extends ColumnWidth
  case object Eight extends ColumnWidth
  case object Nine extends ColumnWidth
  case object Ten extends ColumnWidth
  case object Eleven extends ColumnWidth
  case object Twelve extends ColumnWidth
  val values = findValues
}