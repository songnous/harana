package com.harana.ui.components.cards.vertical

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html.p

@react class PageCard extends StatelessComponent {

	case class Props(showTitle: Boolean,
									 showSocial: Boolean,
									 showMessaging: Boolean,
									 value: String)

	def render() =
		p("PageCard")
}
