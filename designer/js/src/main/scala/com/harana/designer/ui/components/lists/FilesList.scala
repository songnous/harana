package com.harana.ui.components.lists

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

@react class FilesList extends StatelessComponent {

	case class Props(title: String,
									 icon: Option[String] = None,
									 files: List[String] = List.empty,
									 showDownload: Boolean = false,
									 showFileSize: Boolean = false,
									 showOwner: Boolean = false)

	def render() =
		p("SelectElement")

}