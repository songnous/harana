package com.harana.ui.components.elements

import com.harana.ui.components.literal
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

@react class Pad extends StatelessComponent {

	case class Props(left: Int = 0,
									 right: Int = 0,
									 top: Int = 0,
									 bottom: Int = 0)

	def render() = {
		span(style := literal(
			"paddingLeft" -> props.left,
			"paddingRight" -> props.right,
			"paddingTop" -> props.top,
			"paddingBottom" -> props.bottom
		))
	}
}