package com.harana.ui.components.elements

import com.harana.sdk.shared.utils.Random
import com.harana.ui.components.{LinkType, openLink}
import com.harana.ui.external.shoelace.{Button, ButtonGroup, Dropdown, Menu}
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

@react class Toolbar extends StatelessComponent {

	case class Props(toolbarItems: List[HeadingItem])

	def render() =
		ul(className := "icons-list")(
			props.toolbarItems.map { item =>
				li(key := item.id)(
					item match {
						case HeadingItem.Button(button, link, _) =>
							Link(link)(List(button))

						case HeadingItem.IconButton(icon, tooltip, link, className, enabled, _) =>
							Button(
								icon = Some(icon),
								disabled = Some(!enabled),
								className = Some(s"heading-item-button ${className.getOrElse("")}"),
								onClick = Some(_ => openLink(link)),
								tooltip = Some(tooltip)
							)

						case HeadingItem.IconButtonGroup(icons, _) =>
							ButtonGroup(label = Some("button"))(
								icons.map { icon =>
									Button(
										icon = Some(icon.icon),
										disabled = Some(!icon.enabled),
										className = Some(s"heading-item-button ${icon.className.getOrElse("")}"),
										onClick = Some(_ => openLink(icon.link)),
										tooltip = Some(icon.tooltip)
									)
								}
							)

						case HeadingItem.IconMenu(icon, tooltip, dropdownMenuItems, className, enabled, _) =>
							Dropdown(
								button = Some(
									Button.Props(
										disabled = Some(!enabled),
										className = Some(s"heading-item-button ${className.getOrElse("")}"),
										icon = Some(icon),
										caret = Some(true)
									)
								),
								menu = Some(Menu.Props(items = dropdownMenuItems)),
								hoist = Some(true),
								tooltip = Some(tooltip)
							)
					}
				)
			}
		)
}

sealed trait HeadingItem {
	val id: String
}

object HeadingItem {
	case class Button(button: ReactElement,
										link: LinkType,
										id: String = Random.short) extends HeadingItem

	case class IconButton(icon: (String, String),
												tooltip: String,
												link: LinkType,
												className: Option[String] = None,
												enabled: Boolean = true,
												id: String = Random.short) extends HeadingItem

	case class IconButtonGroup(icons: List[IconButton],
														 id: String = Random.short) extends HeadingItem

	case class IconMenu(icon: (String, String),
											tooltip: String,
											menuItems: List[ReactElement] = List(),
											className: Option[String] = None,
											enabled: Boolean = true,
											id: String = Random.short) extends HeadingItem
}