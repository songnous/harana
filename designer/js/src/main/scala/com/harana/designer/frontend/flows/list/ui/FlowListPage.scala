package com.harana.designer.frontend.flows.list.ui

import com.harana.designer.frontend.Circuit
import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.common.grid.ui.{GridPage, GridPageItem}
import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.ui.components.ColumnSize
import com.harana.ui.components.Device.Desktop
import com.harana.ui.components.table.Column
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.web.html.{div, i}

@react object FlowListPage {
  type Props = Unit

  val titleColumn = Column(Some(i"flows.common.title"), Map(Desktop -> ColumnSize.Five))
  val tagsColumn = Column(Some(i"flows.common.tags"), Map(Desktop -> ColumnSize.Four))
  val historyColumn = Column(Some(i"flows.common.history"), Map(Desktop -> ColumnSize.Three))

  val component = FunctionalComponent[Unit] { _ =>
    GridPage(
      entityType = "flows",
      state = Circuit.state(zoomTo(_.flowListState)),
      title = i"heading.section.flows",
      tableColumns = List(titleColumn, tagsColumn, historyColumn),
      tableContent = (column: Column, item: GridPageItem) => div(),
    )
  }
}