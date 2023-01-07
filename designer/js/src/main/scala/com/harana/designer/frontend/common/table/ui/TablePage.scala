package com.harana.designer.frontend.common.table.ui

import com.harana.designer.frontend.common.ui.{FilterItem, filterSection, searchSection}
import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.ui.components.Ref
import com.harana.ui.components.elements.{HeadingItem, NavigationBar, Page}
import com.harana.ui.components.sidebar._
import com.harana.ui.components.table.{Column, GroupedTable, RowGroup}
import com.harana.ui.external.shoelace.Menu
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import com.harana.sdk.shared.utils.Random
import slinky.core.facade.ReactElement

import scala.collection.mutable.ListBuffer


@react class TablePage extends StatelessComponent {

  case class Props(title: String,
                   subtitle: Option[String] = None,
                   subtitleMenu: Option[Menu.Props] = None,
                   navigationBar: Option[ReactElement] = None,
                   fixedNavigationBar: Boolean = true,
                   footerNavigationBar: Option[ReactElement] = None,
                   toolbarItems: List[HeadingItem] = List(),
                   blocked: Boolean = false,
                   activeSearchQuery: Option[String] = None,
                   activeTag: Option[FilterItem] = None,
                   tags: List[FilterItem],
                   columns: List[Column],
                   rowGroups: List[RowGroup],
                   sidebar: Option[Ref[Sidebar]] = None,
                   sidebarAboutItems: List[TextListItem],
                   onSearchQueryChanged: Option[String] => Unit,
                   onTagChanged: Option[FilterItem] => Unit)

  def sidebar = {
    val sections = ListBuffer(searchSection(props.activeSearchQuery, props.onSearchQueryChanged))
    if (props.tags.nonEmpty) sections += filterSection(i"common.sidebar.tags", true, props.tags, props.activeTag, props.onTagChanged)
    sections += SidebarSection(Some(i"files.sidebar.about"), allowCollapse = false, allowClear = false, None, TextListSection(props.sidebarAboutItems))
    Sidebar(sections.toList)
  }

  def table =
    GroupedTable(props.columns, props.rowGroups).withKey(Random.short)

  def render() =
    Page(
      title = props.title,
      subtitle = props.subtitle,
      subtitleMenu = props.subtitleMenu,
      navigationBar = props.navigationBar,
      fixedNavigationBar = props.fixedNavigationBar,
      footerNavigationBar = props.footerNavigationBar,
      toolbarItems = props.toolbarItems,
      blocked = props.blocked,
      noScrollingContent = true,
      leftSidebar = if (props.sidebar.isDefined) props.sidebar else Some(sidebar),
      content = Some(table)
    )
}