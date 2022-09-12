package com.harana.designer.frontend.data.list.ui

import com.harana.designer.frontend.Circuit
import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.common.grid.ui.{GridPage, GridPageItem}
import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup}
import com.harana.sdk.shared.models.data.DataSourceType
import com.harana.ui.components.Device.Desktop
import com.harana.ui.components.table.{Column, HistoryColumn, TagsColumn}
import com.harana.ui.components.ColumnSize
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.web.html.{div, i}

@react object DataSourceListPage {
  type Props = Unit

  val titleColumn = Column(Some(i"datasources.common.title"), Map(Desktop -> ColumnSize.Three))
  val tagsColumn = Column(Some(i"datasources.common.tags"), Map(Desktop -> ColumnSize.Three))
  val typeColumn = Column(Some(i"datasources.common.type"), Map(Desktop -> ColumnSize.Two))
  val historyColumn = Column(Some(i"datasources.common.history"), Map(Desktop -> ColumnSize.Four))

  val component = FunctionalComponent[Unit] { _ =>
    GridPage(
      itemType = "datasources",
      state = Circuit.state(zoomTo(_.dataSourceListState)),
      title = i"heading.section.data",
      tableColumns = List(titleColumn, tagsColumn, typeColumn, historyColumn),
      tableContent = (column: Column, item: GridPageItem) => column match {
        case `titleColumn` => div(item.title)
        case `tagsColumn` => TagsColumn(Set("one", "two"))
        case `typeColumn` =>
          val parts = item.additionalData.getOrElse("type", "").asInstanceOf[String].toLowerCase.split("-")
          if (parts.nonEmpty) div(i"datasources.section.${parts(0)}.${parts(1)}.title") else div()
        case `historyColumn` => HistoryColumn(values = List((1, "value"), (2, "value")), columns = 2)
      },
      editWidth = Some("400px")
    )
  }
}