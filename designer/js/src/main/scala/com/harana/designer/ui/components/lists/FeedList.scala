package com.harana.ui.components.lists

import com.harana.ui.components.cssSet
import com.harana.ui.components.elements.Color
import com.harana.ui.components.{Component, Position}
import enumeratum.values.{StringEnum, StringEnumEntry}
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

@react class FeedList extends StatelessComponent with Component {

	case class Props(items: List[String],
									 shape: FeedListShape = FeedListShape.Circle,
									 style: FeedListStyle = FeedListStyle.Hollow,
									 dateOrTimes: List[String],
									 dateOrTimePosition: Option[Position] = None,
									 lineColor: Option[Color] = None)

	def render() =
		ul(className := s"list-feed ${props.shape.value} ${props.style.value}")(
			props.items.map { item =>
				li(className := cssSet(
					"border-" + props.lineColor.get.value -> props.lineColor.nonEmpty
				))(item)
			}
		)
}

sealed abstract class FeedListShape(val value: String) extends StringEnumEntry
case object FeedListShape extends StringEnum[FeedListShape] {
	case object Circle extends FeedListShape("")
	case object Square extends FeedListShape("list-feed-square")
	case object Rhombus extends FeedListShape("list-feed-rhombus")
	val values = findValues
}

sealed abstract class FeedListStyle(val value: String) extends StringEnumEntry
case object FeedListStyle extends StringEnum[FeedListStyle] {
	case object Solid extends FeedListStyle("list-feed-solid")
	case object Hollow extends FeedListStyle("")
	val values = findValues
}