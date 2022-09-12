package com.harana.ui.components.lists

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

@react class UsersList extends StatelessComponent {

	case class Props(title: String,
									 icon: Option[String] = None,
									 users: List[User] = List.empty,
									 showPosition: Boolean = false,
									 showOnline: Boolean = false,
									 showMessenging: Boolean = false)

	def render() =
		p("SelectElement")
}

case class User()