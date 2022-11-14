package com.harana.designer.frontend.apps.list.ui

import com.harana.designer.frontend.apps.item.AppItemStore.StopApp
import com.harana.designer.frontend.common.grid.ui.{GridPage, GridPageItem}
import com.harana.designer.frontend.Circuit
import com.harana.designer.frontend.Circuit.zoomTo
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.ui.external.shoelace.MenuItem
import com.harana.ui.components.ColumnSize
import com.harana.ui.components.Device.Desktop
import com.harana.ui.components.table.Column
import slinky.web.html.{div, i}

@react object AppListPage {
  type Props = Unit

  val titleColumn = Column(Some(i"apps.common.title"), Map(Desktop -> ColumnSize.Five))
  val tagsColumn = Column(Some(i"apps.common.tags"), Map(Desktop -> ColumnSize.Four))

  val component = FunctionalComponent[Unit] { _ =>
    GridPage(
      entityType = "apps",
      state = Circuit.state(zoomTo(_.appListState)),
      title = i"heading.section.apps",
      tableColumns = List(titleColumn, tagsColumn),
      tableContent = (column: Column, item: GridPageItem) => column match {
        case `titleColumn` => div(item.title)
        case `tagsColumn` => div(item.tags)
      },
      allowDelete = false,
      allowEdit = false,
      itemMenuItems = Some((item: GridPageItem) =>
        List(
          MenuItem(i"apps.menu.stop",
            iconPrefix = Some("lindua", "repeat"),
            onClick = Some(_ => Circuit.dispatch(StopApp(item.id))
          ))
        )
      )
    )
  }
}