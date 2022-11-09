package com.harana.ui.components.sidebar

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

@react class TextSection extends StatelessComponent with SidebarSectionComponent {

	case class Props(textItems: List[TextItem])

	def render() =
		div(className := "category-content")(
			props.textItems.map { item =>
				div(className := "form-group")(
					label(item.title),
					p(item.body)
				)
			}
		)
}

case class TextItem(title: String, body: String)