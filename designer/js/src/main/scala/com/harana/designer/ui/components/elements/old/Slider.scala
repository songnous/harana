	package com.harana.ui.components.elements.old

import enumeratum.{Enum, EnumEntry}
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

@react class SliderElementComponent extends StatelessComponent {

	case class Props(name: String,
									 title: String,
									 minimum: Option[Int],
									 maximum: Option[Int],
									 style: SliderStyle = SliderStyle.Single,
									 from: Option[Int] = None,
									 to: Option[Int] = None,
									 step: Option[Int] = None,
									 grid: Boolean = false,
									 prefix: Option[String] = None,
									 postfix: Option[String] = None,
									 maximumPostfix: Option[String] = None,
									 values: List[Int] = List.empty,
									 valuesSeperator: String = " - ",
									 prettify: Boolean = false,
									 prettifySeperator: Option[String] = None,
									 decorateBoth: Boolean = false)

	def render() =
		p("SelectElement")

}

sealed trait SliderStyle extends EnumEntry
case object SliderStyle extends Enum[SliderStyle] {
	case object Single extends SliderStyle
	case object Double extends SliderStyle
	val values = findValues
}