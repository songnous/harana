package com.harana.ui.components.lists

import com.harana.ui.components.elements.Color
import com.harana.ui.components.Size
import com.harana.ui.external.shoelace.ProgressBar
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

@react class ProgressBarsList extends StatelessComponent {

	case class Props(items: List[(String, Int, Color)],
									 size: Option[Size] = None)

	def render() =
		ul(className := "progress-list")(
			props.items.map { item =>
				li(
					label(
						item._1,
						span(s"${item._2}%")
					),
					//ProgressBar(item._2, showPercentage = false, props.style, props.size, Some(item._3))
				)
			}
		)
}