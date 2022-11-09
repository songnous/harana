package com.harana.ui.components.sidebar

import com.harana.ui.components.{cssSet, when}
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

@react class AccordionSections extends StatelessComponent with SidebarSectionComponent {

	case class Props(items: List[AccordionItem]) {
		val id = (new scala.util.Random).nextInt(1000000)
	}

	def render() =
		div(className := "category-content no-padding")(
			div(id := s"accordion-${props.id}", className := "panel-group")(
				props.items.map { item =>
					div(className := "panel panel-white")(
						div(className := "panel-heading")(
							h6(className := "panel-title text-semibold")(
								a(data-"toggle" := "collapse", data-"parent" := s"accordion-${props.id}", href := s"#accordion-group-${item.id}")(item.title)
							),
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
		)
}

case class AccordionItem(title: String, content: ReactElement, isOpened: Boolean) {
	val id = (new scala.util.Random).nextInt(1000000)
}