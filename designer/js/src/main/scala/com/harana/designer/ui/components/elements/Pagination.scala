package com.harana.ui.components.elements

import com.harana.ui.components.cssSet
import com.harana.ui.components
import enumeratum.values.{StringEnum, StringEnumEntry}
import slinky.core.Component
import slinky.core.annotations.react
import slinky.web.html.{li, _}

@react class Pagination extends Component {

	case class Props(selectedItem: Int,
					 itemCount: Int,
					 itemsToDisplay: Int,
					 position: Option[PaginationPosition] = None,
					 size: Option[PaginationSize] = None,
					 style: Option[PaginationStyle] = None)

	case class State(selectedItem: Int, items: (Int, Int))
	def initialState = State(props.selectedItem, (1, props.itemCount))

	def allowLeft = state.items._1 == 1
	def allowRight = state.items._2 == props.itemCount

	def move(change: Int) =
		if ((change == -1 && allowLeft) || (change == 1 && allowRight))
			setState(State(props.selectedItem, (state.items._1 + change, state.items._2 + change)))

	def render() =
		ul(className := cssSet(
			"pagination" -> true,
			s"pagination-${props.size.map(_.value).orNull}" -> props.style.nonEmpty,
			props.style.get.value -> props.style.nonEmpty
		))(
			li(className := s"${if (allowLeft) "disabled" else ""}")(
				a(href := "#", onClick := (_ => move(-1)))("&lsaquo;")
			),
			List.range(state.items._1, state.items._2).map { i =>
				li(className := s"${if (i == state.selectedItem) "active" else ""}")(
					a(href := "#")(i)
				)
			},
			li(className := s"${if (allowRight) "disabled" else ""}")(
				a(href := "#", onClick := (_ => move(1)))("&rsaquo;")
			)
		)
}

sealed abstract class PaginationPosition(val value: String) extends StringEnumEntry
case object PaginationPosition extends StringEnum[PaginationPosition] {
	case object Left extends PaginationPosition("")
	case object Center extends PaginationPosition("text-center")
	case object Right extends PaginationPosition("text-right")
	val values = findValues
}

sealed abstract class PaginationSize(val value: String) extends StringEnumEntry
case object PaginationSize extends StringEnum[PaginationSize] {
	case object Large extends PaginationSize("xl")
	case object Small extends PaginationSize("sm")
	case object ExtraSmall extends PaginationSize("xs")
	val values = findValues
}

sealed abstract class PaginationStyle(val value: String) extends StringEnumEntry
case object PaginationStyle extends StringEnum[PaginationStyle] {
	case object Flat extends PaginationStyle("pagination-flat")
	case object Separated extends PaginationStyle("pagination-separated")
	case object BorderedRounded extends PaginationStyle("pagination-bordered pagination-rounded")
	case object FlatRounded extends PaginationStyle("pagination-flat pagination-rounded")
	case object SeparatedRounded extends PaginationStyle("pagination-separated pagination-rounded")
	val values = findValues
}