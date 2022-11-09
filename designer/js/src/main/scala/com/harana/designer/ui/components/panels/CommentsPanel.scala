package com.harana.ui.components.panels

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

@react class CommentsPanel extends StatelessComponent {

	case class Props(comments: List[Comment],
									 loggedInUser: User,
									 allowVoting: Boolean = true,
									 allowReplies: Boolean = true)

	def render() =
		p("SelectElement")

}

case class Comment()
case class User()