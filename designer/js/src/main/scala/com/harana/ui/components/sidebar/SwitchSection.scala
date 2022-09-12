package com.harana.ui.components.sidebar

import com.harana.ui.external.shoelace.{Switch => ShoelaceSwitch}
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

case class Switch(label: ReactElement,
									checked: Boolean,
									onClick: Boolean => Unit,
									rightElement: Option[ReactElement] = None)

@react class SwitchSection extends StatelessComponent with SidebarSectionComponent {

	case class Props(switches: List[Switch])

	def render() =
		div(className := "category-content")(
			div(className := "switch-category-content")(
				props.switches.zipWithIndex.map { case (switch, index) =>
					table(
						tr(
							td(className := "switch-section-switch")(
								ShoelaceSwitch(
									className = Some("switch"),
									name = index.toString,
									label = Some(switch.label),
									checked = Some(switch.checked),
									thumbSize = Some("20px"),
									height = Some("16px"),
									width = Some("32px"),
									onChange = Some(switch.onClick))
							),
							td(
								switch.rightElement
							)
						))
				}
			)
		)
}
