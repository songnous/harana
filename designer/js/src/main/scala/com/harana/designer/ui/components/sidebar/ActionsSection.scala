package com.harana.ui.components.sidebar

import com.harana.ui.components._
import com.harana.ui.components.elements.{Color, old}
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

@react class ActionsSection extends StatelessComponent with SidebarSectionComponent {

	case class Props(actions: List[Action] = List())

	def column(position: Int) =
		div(className := "category-content")(
			div(className := "col-xs-6")(
				props.actions.zipWithIndex.filter(_._2 % 2 == position).map { case (action, index) =>
					div()
					//Button(Some(action.title), LinkType.OnClick(action.onClick), icon = Some(action.icon.name), color = action.color)
				}
			)
		)

	def render() =
		div(className := "row")(
			column(0),
			column(1)
		)
}

case class Action(title: String,
                  onClick: () => Unit,
                  icon: String,
                  color: Option[Color])