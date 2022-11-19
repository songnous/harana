package com.harana.designer.frontend.schedules.list.ui

import com.harana.designer.frontend.Circuit
import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.common.grid.ui.{GridPage, GridPageItem}
import com.harana.designer.frontend.schedules.list.ui.actions.ActionRow
import com.harana.designer.frontend.schedules.list.ui.events.EventRow
import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.sdk.shared.models.schedules.{ScheduleExecutionStatus, ScheduleExecutionSummary}
import com.harana.ui.components.ColumnSize
import com.harana.ui.components.Device.Desktop
import com.harana.ui.components.sidebar.SidebarSection
import com.harana.ui.components.table.{Column, TagsColumn}
import com.harana.ui.external.shoelace.{Icon, MenuItem}
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.web.html.{br, className, div, style, table, tbody}

import scala.Range
import scala.scalajs.js.Dynamic.literal

@react object ScheduleListPage {
  type Props = Unit

  val titleColumn = Column(Some(i"schedules.common.title"), Map(Desktop -> ColumnSize.Four))
  val tagsColumn = Column(Some(i"schedules.common.tags"), Map(Desktop -> ColumnSize.Three))
  val lastRunColumn = Column(Some(i"schedules.common.lastrun"), Map(Desktop -> ColumnSize.Two))
  val historyColumn = Column(Some(i"schedules.common.history"), Map(Desktop -> ColumnSize.Three))

  val component = FunctionalComponent[Unit] { _ =>
    val state = Circuit.state(zoomTo(_.scheduleListState))
    val dataState = Circuit.state(zoomTo(_.dataSourceListState))
    val flowState = Circuit.state(zoomTo(_.flowListState))

    val green = "#26a69a"
    val red = "rgb(184,65,102)"
    val orange = "rgb(207,144,58)"
    val grey = "rgb(200,200,200)"
    
    GridPage(
      entityType = "schedules",
      state = state,
      title = i"heading.section.schedules",
      itemMenuItems = Some((item: GridPageItem) =>
        List(
          MenuItem(i"apps.menu.stop",
            iconPrefix = Some("lindua", "repeat"),
            onClick = None)
          )
      ),
      tableColumns = List(titleColumn, tagsColumn, lastRunColumn, historyColumn),
      tableContent = (column: Column, item: GridPageItem) => column match {
        case `titleColumn`    => div(item.title)
        case `tagsColumn`     => TagsColumn(Set("tag-alpha", "tag-beta"))
        case `lastRunColumn`  => div(
                                  "2.2 min",
                                  Icon(library = Some("icomoon"), name = "checkmark3", className = Some("schedule-success"))
                                )
        case `historyColumn`  =>
          val executions = item.additionalData("recentExecutions").asInstanceOf[List[ScheduleExecutionSummary]]
          div(
            executions.map { execution =>
              val color = execution.executionStatus match {
                case ScheduleExecutionStatus.None                  => grey
                case ScheduleExecutionStatus.Executing             => green
                case ScheduleExecutionStatus.PendingCancellation   => red
                case ScheduleExecutionStatus.PendingExecution      => orange
                case ScheduleExecutionStatus.Failed                => red
                case ScheduleExecutionStatus.Killed                => red
                case ScheduleExecutionStatus.Cancelled             => red
                case ScheduleExecutionStatus.Initialised           => grey
                case ScheduleExecutionStatus.Paused                => orange
                case ScheduleExecutionStatus.Succeeded             => green
                case ScheduleExecutionStatus.TimedOut              => red
              }
              div(className := "schedules-pill", style := literal("background-color" -> color))
            },
            Range(executions.size, 20).map { _ =>
              div(className := "schedules-pill", style := literal("background-color" -> grey))
            }

          )
      },
      sidebarSections = List(
        SidebarSection(
          Some(i"schedules.sidebar.history"),
          className = Some("schedules-history"),
          content = state.additionalState.scheduleHistory.map(pair =>
            div(className := "item")(
              div(className := "status")(pair._1.id),
              div(className := "title") (pair._2.title)
            )
          ),
        )
      ),
      editWidth = Some("500px"),
      editAdditionalSections = List(
        (i"schedules.groups.events", div(className := "schedule-editor")(
          table(tbody(state.additionalState.itemEvents.zipWithIndex.map { case (a, index) =>
            EventRow(index, Some(a), allowDelete = state.additionalState.itemEvents.size > 1) }))
          )
        ),
        (i"schedules.groups.actions", div(className := "schedule-editor")(
          table(tbody(state.additionalState.itemActions.zipWithIndex.map { case (a, index) =>
            ActionRow(index, Some(a), dataState.entities, flowState.entities, state.entities, allowDelete = state.additionalState.itemActions.size > 1) }))
          )
        )
      )
    )
  }
}