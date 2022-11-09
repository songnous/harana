package com.harana.ui.components.lists

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

@react class ImagesList extends StatelessComponent {

	case class Props(title: String,
									 icon: Option[String] = None,
									 images: List[String] = List.empty,
									 columns: Int)

	def render() =
		p("SelectElement")

}