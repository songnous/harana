package com.harana.ui.components.elements

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

@react class LineSeperator extends StatelessComponent {

	case class Props()

	def render() =
		hr()

}