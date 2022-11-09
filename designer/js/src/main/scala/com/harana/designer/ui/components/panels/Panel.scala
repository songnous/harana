package com.harana.ui.components.panels

import com.harana.ui.components.when
import com.harana.ui.components.elements.Color
import enumeratum.values.{StringCirceEnum, StringEnum, StringEnumEntry}
import enumeratum.{CirceEnum, Enum, EnumEntry}
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

@react class Panel extends StatelessComponent {

	case class Props(title: String,
									 body: ReactElement,
									 leftOfTitleElement: Option[ReactElement] = None,
									 rightOfTitleElement: Option[ReactElement] = None,
									 rightElement: Option[ReactElement] = None,
									 titleSize: Option[TitleSize] = None,
									 style: Option[Style] = None,
									 headerColor: Option[Color] = None,
									 footer: Option[(String, ReactElement)] = None)

	def headingElement = {
		div(className := "panel-heading")(
			props.leftOfTitleElement.getOrElse(span()),
			props.title,
			props.rightOfTitleElement.getOrElse(span()),

			when(props.rightElement,
				div(className := "heading-elements")(
					props.rightElement
				)
			)
		)
	}

	def render() =
		div(className := "panel panel-flat")(
			div(className := "panel-heading")(
				props.titleSize match {
					case Some(size) => {
						size match {
							case TitleSize.H1 => h1(className := "panel-title")(headingElement)
							case TitleSize.H2 => h2(className := "panel-title")(headingElement)
							case TitleSize.H3 => h3(className := "panel-title")(headingElement)
							case TitleSize.H4 => h4(className := "panel-title")(headingElement)
							case TitleSize.H5 => h5(className := "panel-title")(headingElement)
							case TitleSize.H6 => h6(className := "panel-title")(headingElement)
						}
					}
					case None => h6(className := "panel-title")(headingElement)
				}
			),
			div(className := "panel-body")(props.body),
			when(props.footer,
				div(className := "panel-footer")(
					div(className := "heading-elements")(
						span(className := "heading-text text-semibold")(props.footer.get._1),
						span(className := "heading-text pull-right")(props.footer.get._2)
					)
				)
			)
		)
}

sealed abstract class Style(val value: String) extends StringEnumEntry
case object Style extends StringEnum[Style] with StringCirceEnum[Style] {
	case class Bordered(size: Option[BorderSize] = None,
											topColor: Option[Color] = None,
											bottomColor: Option[Color] = None,
											leftColor: Option[Color] = None,
											rightColor: Option[Color] = None) extends Style("panel-group-control")
	case class Solid(color: Color) extends Style(s"bg-${color.value}")
	val values = findValues
}

sealed abstract class BorderSize(val value: String) extends StringEnumEntry
case object BorderSize extends StringEnum[BorderSize] with StringCirceEnum[BorderSize] {
	case object Large extends BorderSize("lg")
	case object ExtraLarge extends BorderSize("xlg")
	val values = findValues
}

sealed trait TitleSize extends EnumEntry
case object TitleSize extends Enum[TitleSize] with CirceEnum[TitleSize] {
	case object H1 extends TitleSize
	case object H2 extends TitleSize
	case object H3 extends TitleSize
	case object H4 extends TitleSize
	case object H5 extends TitleSize
	case object H6 extends TitleSize
	val values = findValues
}