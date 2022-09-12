package com.harana.ui.components.elements.old

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html.div

import scala.scalajs.js

@react class CodeEditorModal extends StatelessComponent {

	case class Props(title: String,
									 code: Option[String] = None,
									 language: Option[String] = None,
									 availableLanguages: List[String] = List(),
									 snippets: List[CodeSnippet] = List(),
									 versions: List[CodeVersion] = List())

	def render() = {
		div()
//		// Dropdown menus
//		props.snippets.map {
//			case CodeSnippetItem(title, id) => {
//				//Menu(List())
//			}
//
//			case CodeSnippetGroup(title) => {
//				//Menu(List())
//			}
//		}
	}
}

case class CodeVersion(date: js.Date, description: String)

sealed trait CodeSnippet { val title: String }
case class CodeSnippetItem(title: String, id: String) extends CodeSnippet
case class CodeSnippetGroup(title: String) extends CodeSnippet