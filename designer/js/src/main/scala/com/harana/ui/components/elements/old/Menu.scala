package com.harana.ui.components.elements.old

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html.div

@react class Menu extends StatelessComponent {

type Props = Unit

	//	case class Props(groups: List[MenuGroup],
//									 onSelect: MenuItem => Unit)

	def render() =
		div()

	/*
	def render() = {
		val items: List[ReactElement] =
			if (props.groups.size == 1)
				props.groups.head.items.map(convert)
			else
				props.groups.map { group =>
					val items = new ListBuffer()
					if (group.label.isDefined) items += SLMenuLabel(Some(group.label.get))
					items ++= group.items.map(convert)
					items += SLMenuDivider()
					items.toList
				}

		SLMenu(items)
	}

	def convert(menuItem: MenuItem): Ref[SLMenuItem] =
		SLMenuItem(checked = menuItem.checked, disabled = menuItem.disabled, iconPrefix = menuItem.icon, label = Some(menuItem.label), value = menuItem.value)
	*/
}

case class MenuGroup(label: Option[String],
										 items: List[MenuItem])

case class MenuItem(label: String,
										checked: Option[Boolean] = None,
										disabled: Option[Boolean] = None,
										icon: Option[String] = None,
									  value: Option[String] = None)