package com.harana.ui.components.elements.old

import com.harana.ui.components.elements.Color
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

@react class Tag extends StatelessComponent {

	case class Props(color: Option[Color] = None)

	def render() =
		input(`type` := "text", className := "form-control tokenfield input-lg", value := "Red,blue,green")
}