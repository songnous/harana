package com.harana.designer.frontend.schedules.list.ui

import com.harana.designer.frontend.Circuit
import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.common.grid.ui.{GridPage, GridPageItem}
import com.harana.designer.frontend.schedules.list.ui.actions.ActionRow
import com.harana.designer.frontend.schedules.list.ui.events.EventRow
import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.ui.components.ColumnSize
import com.harana.ui.components.Device.Desktop
import com.harana.ui.components.table.{Column, HistoryColumn, TagsColumn}
import io.circe.parser._
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.web.html.{className, div, table, tbody}

@react object ScheduleListPage {
  type Props = Unit

  val titleColumn = Column(Some(i"schedules.common.title"), Map(Desktop -> ColumnSize.Five))
  val tagsColumn = Column(Some(i"schedules.common.tags"), Map(Desktop -> ColumnSize.Four))
  val historyColumn = Column(Some(i"schedules.common.history"), Map(Desktop -> ColumnSize.Three))

  val component = FunctionalComponent[Unit] { _ =>
    val state = Circuit.state(zoomTo(_.scheduleListState))
    val dataState = Circuit.state(zoomTo(_.dataSourceListState))
    val flowState = Circuit.state(zoomTo(_.flowListState))

    GridPage(
      itemType = "schedules",
      state = state,
      title = i"heading.section.schedules",
      tableColumns = List(titleColumn, tagsColumn, historyColumn),
      tableContent = (column: Column, item: GridPageItem) => column match {
        case `titleColumn` => div(item.title)
        case `tagsColumn` => TagsColumn(Set("one", "two"))
        case `historyColumn` => HistoryColumn(values = List((1, "value"), (2, "value")), columns = 2)
      },
      editWidth = Some("500px"),
      editAdditionalSections = List(
        (i"schedules.groups.events", div(className := "schedule-editor")(
          table(tbody(state.editState.itemEvents.zipWithIndex.map { case (a, index) =>
            EventRow(index, Some(a), allowDelete = state.editState.itemEvents.size > 1) }))
          )
        ),
        (i"schedules.groups.actions", div(className := "schedule-editor")(
          table(tbody(state.editState.itemActions.zipWithIndex.map { case (a, index) =>
            ActionRow(index, Some(a), dataState.entities, flowState.entities, state.entities, allowDelete = state.editState.itemActions.size > 1) }))
          )
        )
      )
    )
  }
}