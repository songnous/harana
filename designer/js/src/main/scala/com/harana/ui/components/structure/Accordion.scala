package com.harana.ui.components.structure

import com.harana.ui.components._
import enumeratum.values.{StringCirceEnum, StringEnum, StringEnumEntry}
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

@react class Accordion extends StatelessComponent {

	case class Props(items: List[AccordionItem],
									 allowRefresh: Boolean,
									 allowMove: Boolean,
									 allowClose: Boolean,
									 allowMultiple: Boolean,
									 expandControl: Option[ExpandControlPosition] = None) {
		val id = (new scala.util.Random).nextInt(1000000)
	}

	def render() =
		div(id := s"accordion-${props.id}", className := cssSet(
			"panel-group" -> true,
			"content-group-lg" -> true,
			"accordion-sortable" -> props.allowMove,
			optEnum(props.expandControl) -> props.expandControl.isDefined))(
				props.items.map { item =>
					div(className := "panel panel-white")(
						div(className := "panel-heading")(
							h6(className := "panel-title")(
								a(data-"toggle" := "collapse", data-"parent" := s"accordion-${props.id}", href := s"#accordion-group-${item.id}")(item.title)
							),
							when(props.allowRefresh || props.allowMove || props.allowClose)(
								div(className := "heading-elements")(
									ul(className := "icons-list")(
										when(props.allowRefresh, li(a(data-"action" := "reload"))),
										when(props.allowMove, li(a(data-"action" := "move"))),
										when(props.allowClose, li(a(data-"action" := "close")))
									)
								)
							)
						),
						div(id := s"accordion-group-${item.id}", className := cssSet(
							"panel-collapse" -> true,
							"collapse" -> true,
							"in" -> item.isOpened))(
							div(className := "panel-body")(item.content)
						)
					)
				}
			)
}

case class AccordionItem(title: String, content: ReactElement, isOpened: Boolean) {
	val id = (new scala.util.Random).nextInt(1000000)
}

sealed abstract class ExpandControlPosition(val value: String) extends StringEnumEntry
case object ExpandControlPosition extends StringEnum[ExpandControlPosition] with StringCirceEnum[ExpandControlPosition] {
	case object Left extends ExpandControlPosition("panel-group-control")
	case object Right extends ExpandControlPosition("panel-group-control-right")
	val values = findValues
}
