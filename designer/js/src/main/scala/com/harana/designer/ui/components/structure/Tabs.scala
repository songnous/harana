package com.harana.ui.components.structure

import com.harana.ui.components.cssSet
import com.harana.ui.components._
import com.harana.ui.components.elements.{Color, Item, ItemPosition, ItemType}
import enumeratum.values.{StringCirceEnum, StringEnum, StringEnumEntry}
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._


@react class TabsStructure extends StatelessComponent {

	case class Props(items: List[(TabItem, ReactElement)],
									 activeItem: TabItem,
									 size: Option[Size] = None,
									 style: Option[Style] = None,
									 position: Option[Position] = None,
									 border: Option[Border] = None) {
		val random = new scala.util.Random()
		val ids = items.map { _ => random.nextInt(100000) }
	}

	def render() =
		div(className := "tabbable")(
			ul(className := "nav nav-tabs")(
				props.items.zipWithIndex.map { case (item, index) =>
					li(className := (if (item._1.equals(props.activeItem)) "active" else ""))(
						a(href := s"#tab-${props.ids(index)}", data-"toggle" := "tab")(
							Item(item._1.title, item._1.item)
						)
					)
				}
			),
			div(className := "tab-content")(
				props.items.zipWithIndex.map { case (item, index) =>
					div(className := cssSet(
						"tab-pane" -> true,
						"active" -> item._1.equals(props.activeItem)),
						id := s"#tab-${props.ids(index)}")(
						item._2
					)
				}
			)
		)
}

case class TabItem(title: Option[String],
									 item: Option[(ItemType, ItemPosition)] = None)

sealed abstract class Style(val value: String) extends StringEnumEntry
case object Style extends StringEnum[Style] with StringCirceEnum[Style] {
	case object TopLine extends Style("nav-tabs-top")
	case object TopLineDivided extends Style("top-divided")
	case object TopLineJustified extends Style("nav-justified nav-tabs-top")
	case object TopLineDividedAndJustified extends Style("nav-justified nav-tabs-top top-divided")

	case object BottomLine extends Style("nav-tabs-bottom")
	case object BottomLineDivided extends Style("bottom-divided")
	case object BottomLineJustified extends Style("nav-justified nav-tabs-bottom")
	case object BottomLineDividedAndJustified extends Style("nav-justified nav-tabs-bottom bottom-divided")

	case object Solid extends Style("nav-tabs-solid")
	case object SolidJustified extends Style("nav-justified nav-tabs-solid")
	case object SolidRounded extends Style("nav-tabs-component")
	case object SolidJustifiedAndRounded extends Style("nav-justified nav-tabs-solid nav-tabs-component")

	case class Colored(color: Color) extends Style(s"bg-${color.value}")
	case class ColoredJustified(color: Color) extends Style(s"nav-justified bg-${color.value}")
	case class ColoredRounded(color: Color) extends Style(s"nav-tabs-component bg-${color.value}")
	case class ColoredJustifiedAndRounded(color: Color) extends Style(s"nav-justified nav-tabs-component bg-${color.value}")

	val values = findValues
}

sealed abstract class Position(val value: String) extends StringEnumEntry
case object Position extends StringEnum[Position] with StringCirceEnum[Position] {
	case object HorizontalLeft extends Position("")
	case object HorizontalCentre extends Position("text-center")
	case object HorizontalRight extends Position("text-right")
	case object VerticalLeft extends Position("nav-tabs-vertical nav-tabs-left")
	case object VerticalRight extends Position("nav-tabs-vertical nav-tabs-right")
	val values = findValues
}

sealed abstract class Border(val value: String) extends StringEnumEntry
case object Border extends StringEnum[Border] with StringCirceEnum[Border] {
	case object Standard extends Border("tab-content-bordered")
	case object Justified extends Border("nav-justified tab-content-bordered")
	val values = findValues
}