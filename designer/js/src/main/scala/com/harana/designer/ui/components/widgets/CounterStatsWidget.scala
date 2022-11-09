package com.harana.ui.components.widgets

import com.harana.ui.components._
import com.harana.ui.components.elements.Color
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

@react class CounterStatsWidget extends StatelessComponent {

	case class Props(value: String,
									 valueName: String,
									 icon: Option[String] = None,
									 iconPosition: HorizontalPosition = HorizontalPosition.Left,
									 color: Option[Color] = None,
									 coloredBackground: Boolean = false,
									 relatedValues: List[Value] = List())

	def render() =
		p("SelectElement")


	//		div(cssSet(
//			"panel" -> truediv(className := "table-responsive")(
//			"panel-body" -> truediv(className := "table-responsive")(
//			"panel-body-accent" -> truediv(className := "table-responsive")(
//			"has-bg-image" -> model.coloredBackground)div(className := "table-responsive")(
//			div(className := "media no-margin",
//				div(className := "media-left media-middle",
//					i(className := "icon-pointer icon-3x text-success-400")
//				),
//				div(className := "media-body text-right",
//					h3(className := "no-margin text-semibold",
//						model.value
//					),
//					span(className := "text-uppercase text-size-mini text-muted", model.valueName)
//				)
//			)
//		)
//	})

}