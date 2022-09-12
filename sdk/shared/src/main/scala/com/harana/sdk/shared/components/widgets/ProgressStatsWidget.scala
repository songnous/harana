package com.harana.sdk.shared.components.widgets

import com.harana.sdk.shared.components._
import com.harana.sdk.shared.models.common.Component
import io.circe.generic.JsonCodec
import enumeratum._

@JsonCodec
case class ProgressStatsWidget(title: String,
            subtitle: String,
            percentages: List[Percentage],
            circular: Boolean,
            icon: Option[String],
            color: Color,
            colorAccent: ColorAccent,
            coloredBackground: Boolean,
            relatedValues: List[Value] = List()) extends Component

sealed trait ProgressStyle extends EnumEntry
case object ProgressStyle extends Enum[ProgressStyle] with CirceEnum[ProgressStyle] {
  case object Horizontal extends ProgressStyle
  case object Circular extends ProgressStyle
  case object Half extends ProgressStyle
  case object Speedometer extends ProgressStyle
  val values = findValues
}