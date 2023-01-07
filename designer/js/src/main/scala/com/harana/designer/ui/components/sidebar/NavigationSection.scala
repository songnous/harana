package com.harana.ui.components.sidebar

import com.harana.ui.components.elements.Color
import com.harana.ui.components.when
import com.harana.ui.external.shoelace.Tag
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

@react class NavigationSection extends StatelessComponent with SidebarSectionComponent {

	case class Props(groups: List[NavigationGroup])

	def renderNavigationItem(item: NavigationItem): ReactElement =
		li(className := (if (item.isActive) "active" else ""))(
			a(onClick := item.onClick)(
				when(item.icon, i(className := item.icon.get)),
				if (item.isPill) Tag(label = item.title, pill = Some(true), size = Some("medium"), className = Some("text-muted")) else span(item.title),
				when(item.rightText, span(className := "text-muted text-size-small text-regular pull-right")(item.rightText.get)),
				when(item.badge, span(className := s"badge bg-${item.badgeColor.get.value}")(item.badge.get)),
				when(item.label, span(className := s"label bg-${item.labelColor.get.value}")(item.label.get))
			),
			when(item.subItems.nonEmpty,
				ul(
					item.subItems.map(renderNavigationItem)
				)
			)
		)

	def render() =
		div(className := "category-content no-padding")(
			ul(className := "navigation navigation-alt navigation-accordion")(
				props.groups.flatMap { group =>
					List[ReactElement](
						when(group.title, li(className := "navigation-header")(span(group.title.get))),
						group.items.map(renderNavigationItem)
					)
				}
			)
		)
}

case class NavigationGroup(items: List[NavigationItem],
													 title: Option[String] = None)

case class NavigationItem(title: String,
													onClick: () => Unit,
													subItems: List[NavigationItem] = List(),
													isActive: Boolean = false,
													isPill: Boolean = false,
													icon: Option[String] = None,
													rightText: Option[String] = None,
													badge: Option[Int] = None,
													badgeColor: Option[Color] = None,
													label: Option[String] = None,
													labelColor: Option[Color] = None)