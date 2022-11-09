package com.harana.ui.components.sidebar

import com.harana.ui.components.elements.old.ProgressBar
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

@react class ProgressBarsSection extends StatelessComponent with SidebarSectionComponent {

	case class Props(progressBars: List[(String, ProgressBar)])

	def render() =
		div(className := "category-content")(
			props.progressBars.map { progressBar =>
				div(className := "content-group")(
					label(progressBar._1),
					progressBar._2.render()
				)
			}
		)
}