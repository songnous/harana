package com.harana.ui.components.lists

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

@react class EventsList extends StatelessComponent {

	case class Props(title: String,
									 icon: Option[String] = None,
									 events: List[String] = List.empty)

	def render() =
		p("SelectElement")

}