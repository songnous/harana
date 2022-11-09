package com.harana.ui.components.elements

import com.harana.designer.frontend.utils.DateUtils
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html.{className, div, p}

import java.time.Instant

@react class Date extends StatelessComponent {

	case class Props(instant: java.time.Instant)

	def render() =
		div(className := "hover-content date-column")(
			p(className := "not-hovered")(
				s"${DateUtils.pretty(props.instant, Instant.now)} ago"
			),
			p(className := "hovered")(
				DateUtils.format(props.instant, includeTime = true)
			)
		)
}