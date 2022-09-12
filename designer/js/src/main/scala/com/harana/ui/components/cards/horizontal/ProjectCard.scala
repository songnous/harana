package com.harana.ui.components.cards.horizontal

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

@react class ProjectCard extends StatelessComponent {

	case class Props(title: String,
									 subtitle1: String,
									 subtitle1Url: String,
									 subtitle2: String,
									 subtitle2Url: String,
									 description: String,
									 thumbnailUrl: String,
									 linkUrl: String,
									 isNew: Boolean,
									 lastUpdated: String)

	def render() = div()

}
