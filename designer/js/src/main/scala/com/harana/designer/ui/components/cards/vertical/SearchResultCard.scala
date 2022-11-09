package com.harana.ui.components.cards.vertical

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html.p

@react class SearchResultCard extends StatelessComponent {

	case class Props(title: String,
									 subtitleOne: String,
									 subtitleTwo: String,
									 description: String,
									 lastUpdated: String,
									 icon: String)

	def render() =
		p("SearchResultCard")

}
