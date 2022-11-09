package com.harana.ui.components.sidebar

import com.harana.sdk.shared.utils.Random
import com.harana.ui.components.{cssSet, emptyElement, when}
import com.harana.ui.external.shoelace.{Icon, IconButton, Menu}
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.{Fragment, ReactElement}
import slinky.web.html._

import scala.scalajs.js

@react class Sidebar extends StatelessComponent {

	case class Props(sections: List[SidebarSection] = List(),
									 tabs: List[Tab] = List(),
									 activeTab: Option[Tab] = None,
									 onChangeTab: Option[Tab => Unit] = None,
									 separateCategories: Boolean = true,
									 fixed: Boolean = false,
									 showBorder: Boolean = true,
									 padding: Boolean = true)

	def render() =
		div(className := cssSet("sidebar sidebar-main sidebar-default" -> true, "sidebar-separate" -> props.separateCategories, "no-padding" -> !props.padding))(
			if (props.fixed) div(className := "sidebar-fixed")(renderContent) else renderContent
		)

	def isTabActive(tab: Tab) =
		props.activeTab.isDefined && props.activeTab.get.name == tab.name

	def renderContent =
		if (props.tabs.isEmpty)
			div(className := cssSet("sidebar-content" -> props.showBorder))(props.sections.map(renderSection))
		else
			div(className := cssSet("sidebar-content" -> props.showBorder))(
				div(className := "tabbable sortable ui-sortable")(
					ul(className := cssSet("nav nav-lg nav-tabs nav-justified"-> true, "no-margin" -> !props.padding))(
						props.tabs.map { tab =>
							li(className := cssSet("active" -> isTabActive(tab), "dropdown" -> tab.menu.isDefined), key := tab.name)(
								a(onClick := (() => if (props.onChangeTab.isDefined) props.onChangeTab.get(tab)), className := "dropdown-toggle")(
									when(tab.icon, Icon(Some(tab.icon.get._1), Some(tab.icon.get._2), name = tab.icon.get._1)),
									span(className := "visible-xs-inline-block position-right")(tab.name),
									if (props.tabs.isEmpty) span(className := "caret") else emptyElement
								)
							)
						}
					),
					div(className := "tab-content")(
						props.tabs.map { tab =>
							div(key := tab.name, className := cssSet("tab-pane fade no-padding" -> true, "active in" -> isTabActive(tab)))(
								tab.sections.map(renderSection)
							)
						}
					)
				)
			)

	def renderSection(section: SidebarSection) =
		div(key := section.id, className := "sidebar-category")(
			if (section.title.isDefined)
				Fragment(
					div(className := "category-title")(
						span(section.title),
						when(section.allowClear, ul(className := "icons-list")(li(IconButton(library = Some("icomoon"), name = "cross2", onClick = section.onClear)))),
						when(section.allowCollapse, ul(className := "icons-list")(li(a(href := "#", data-"action" := "collapse"))))
					),
					section.content)
			else
				section.content
		)
}

trait SidebarSectionComponent {
	def render(): ReactElement
}

case class Tab(name: String,
							 sections: List[SidebarSection] = List(),
							 icon: Option[(String, String)] = None,
							 menu: Option[Menu.Props] = None)

case class SidebarSection(title: Option[String] = None,
													allowCollapse: Boolean = false,
													allowClear: Boolean = false,
													onClear: Option[js.Any => Unit] = None,
													content: ReactElement,
													id: String = Random.short)