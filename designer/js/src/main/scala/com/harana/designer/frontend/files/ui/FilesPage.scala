package com.harana.designer.frontend.files.ui

import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.common.preview.ui.PreviewPage
import com.harana.designer.frontend.common.table.ui.TablePage
import com.harana.designer.frontend.files.FilesStore._
import com.harana.designer.frontend.navigation.ui.Navigation
import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.designer.frontend.utils.SizeUtils
import com.harana.designer.frontend.{Circuit, Main}
import com.harana.ui.components.elements.Dialog
import com.harana.ui.components.sidebar.TextListItem
import com.harana.ui.components.table.{Column, Row, RowGroup}
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.{Fragment, React}

import scala.scalajs.js

@react object FilesPage {

  val dialogRef = React.createRef[Dialog.Def]

  val component = FunctionalComponent[js.Dynamic] { props =>
    val state = Circuit.state(zoomTo(_.filesState))
    val filesState = Circuit.state(zoomTo(_.filesState))
    val userState = Circuit.state(zoomTo(_.userState))

    val subtitle = if (state.item.isDefined && state.item.get.path != "") Some(state.item.get.name) else None

    Fragment(
      Dialog.withRef(dialogRef),
      state.item match {
        case Some(item) if !item.isFolder =>
          PreviewPage(
            file = item,
            path = state.path,
            title = i"heading.section.files",
            subtitle = subtitle,
            preview = state.itemPreview,
            navigationBar = Some(Navigation(())),
            toolbarItems = List(toolbar.pathTree(state)),
            blocked = state.blocked,
            sidebarAboutItems = List(
              TextListItem(i"files.sidebar.updated", state.item.map(d => Left(d.updated))),
              TextListItem(i"files.sidebar.size", state.item.map(s => Right(SizeUtils.format(s.size)))),
              TextListItem(i"files.sidebar.owner", Some(Right(s"${Main.claims.firstName} ${Main.claims.lastName}"))),
              TextListItem("", None),
            ))

        case _ =>
          TablePage(
            title = i"heading.section.files",
            subtitle = subtitle,
            navigationBar = Some(Navigation(())),
            toolbarItems = (
              if (state.path.nonEmpty)
                List(toolbar.pathTree(state))
              else List()) ++ List(toolbar.edit(dialogRef, state), toolbar.create(dialogRef, state), toolbar.sort),
            blocked = state.blocked,
            activeSearchQuery = state.searchQuery,
            activeTag = state.tag,
            tags = state.tags,
            columns = table.columns,
            rowGroups = List(RowGroup(None, table.rows(state))),
            sidebar = if (state.path.isEmpty) Some(sidebar.home(filesState, userState, dialogRef)) else None,
            sidebarAboutItems = List(
              TextListItem(i"files.sidebar.updated", state.item.map(d => Left(d.updated))),
              TextListItem(i"files.sidebar.size", state.item.map(s => Right(SizeUtils.format(s.size)))),
              TextListItem(i"files.sidebar.owner", Some(Right(s"${Main.claims.firstName} ${Main.claims.lastName}"))),
//              TextListItem(i"files.sidebar.tags", s"")
            ),
            onSearchQueryChanged = query => Circuit.dispatch(UpdateSearchQuery(query)),
            onTagChanged = tag => Circuit.dispatch(UpdateTag(tag))
          )
      }
    )
  }
}