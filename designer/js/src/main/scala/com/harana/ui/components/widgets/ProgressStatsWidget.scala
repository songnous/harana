package com.harana.ui.components.widgets

import com.harana.ui.components._
import com.harana.ui.components.elements.Color
import enumeratum.{CirceEnum, Enum, EnumEntry}
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

@react class ProgressStatsWidget extends StatelessComponent {

	case class Props(title: String,
									 subtitle: String,
									 percentages: List[Percentage],
									 circular: Boolean,
									 icon: Option[String],
									 color: Color,
									 coloredBackground: Boolean,
									 relatedValues: List[Value] = List())

	def render() =
		p("SelectElement")

}

sealed trait ProgressStyle extends EnumEntry
case object ProgressStyle extends Enum[ProgressStyle] with CirceEnum[ProgressStyle] {
	case object Horizontal extends ProgressStyle
	case object Circular extends ProgressStyle
	case object Half extends ProgressStyle
	case object Speedometer extends ProgressStyle
	val values = findValues
}