package com.harana.ui.components.elements

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

@react class PasswordGenerator extends StatelessComponent {

	case class Props(name: String,
									 title: String,
									 checked: Boolean)

	def render() =
		p("SelectElement")

}