package com.harana.ui.components.elements

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

@react class PercentageChange extends StatelessComponent {

	case class Props(change: Double)

	private def change(cls: String, icon: String, value: String) = {
		span(className := s"text-$cls")(
			i(className := s"icon-$icon position-left")(
				s"$value%"
			)
		)
	}

	def render() =
		props.change match {
			case x if x == 0.0 => change("", "", s"${props.change}")
			case x if x > 0.0 => change("success-600", "stats-growth2", s"${props.change}")
			case x if x < 0.0 => change("danger", "stats-decline2", s"-${props.change}")
		}
}