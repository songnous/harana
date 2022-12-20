package com.harana.designer.ui.components.elements

import com.harana.designer.frontend.utils.DateUtils
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.web.html.{className, div, p}

import java.time.Instant

@react object PrettyDate {

	case class Props(instant: Instant, instant2: Option[Instant] = None)

	val component = FunctionalComponent[Props] { props =>
		div(className := "hover-content date-column")(
			p(className := "not-hovered")(
				s"${DateUtils.pretty(props.instant, props.instant2.getOrElse(Instant.now))} ago"
			),
			p(className := "hovered")(
				DateUtils.format(props.instant, includeTime = true)
			)
		)
	}
}