package com.harana.ui.components.sidebar

import com.harana.ui.external.shoelace.Input
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

import scala.scalajs.js.timers._

@react class SearchSection extends StatelessComponent with SidebarSectionComponent {

	var timeout: Option[SetTimeoutHandle] = None

	case class Props(onSearch: Option[String] => Unit,
									 debounce: Boolean = true,
									 minimumLength: Int = 2,
									 value: Option[String] = None)

	// FIXME: Why do we need SearchSection prefix for getClass ?
	def render() =
		div(className := "category-content")(
			form(action := "#")(
				div(className := "has-feedback has-feedback-left")(
					Input(
						autoCapitalize = Some("off"),
						autoComplete = Some("off"),
						autoCorrect = Some("off"),
						name = s"${SearchSection.getClass.getSimpleName}-search",
						onInput = Some(onInput),
						clearable = Some(false),
						iconPrefix = Some("icomoon", "search"),
						size = Some("large"),
						value = props.value
					)
				)
			)
		)

	private def onInput(text: String): Unit = {
		val optText = if (text.isEmpty || text.length < props.minimumLength) None else Some(text)
		if (props.debounce) {
			if (timeout.nonEmpty) clearTimeout(timeout.get)
			timeout = Some(setTimeout(500) {
				props.onSearch(optText)
			})
		} else {
			props.onSearch(optText)
		}
	}
}