package com.harana.ui.components.cards.vertical

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html.p

@react class QuestionCard extends StatelessComponent {

	case class Props()

	def render() =
		p("QuestionCard")
}
