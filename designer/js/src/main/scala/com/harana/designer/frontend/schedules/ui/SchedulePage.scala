package com.harana.designer.frontend.schedules.ui

import com.harana.designer.frontend.Circuit
import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.common.grid.ui.{GridPage, GridPageItem}
import com.harana.designer.frontend.schedules.ui.actions.ActionRow
import com.harana.designer.frontend.schedules.ui.events.EventRow
import com.harana.designer.frontend.utils.DateUtils
import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.designer.ui.components.elements.PrettyDate
import com.harana.sdk.shared.models.schedules.{Schedule, ScheduleExecution, ScheduleExecutionStatus}
import com.harana.ui.components.ColumnSize
import com.harana.ui.components.Device.Desktop
import com.harana.ui.components.elements.Drawer
import com.harana.ui.components.sidebar.SidebarSection
import com.harana.ui.components.table.{Column, TagsColumn}
import com.harana.ui.external.shoelace.{MenuItem, Tooltip}
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.{Fragment, React}
import slinky.web.html._

import scala.scalajs.js.Dynamic.literal

@react object SchedulePage {
  type Props = Unit

  val titleColumn = Column(Some(i"schedules.common.title"), Map(Desktop -> ColumnSize.Four))
  val tagsColumn = Column(Some(i"schedules.common.tags"), Map(Desktop -> ColumnSize.Three))
  val historyColumn = Column(Some(i"schedules.common.history"), Map(Desktop -> ColumnSize.Five))

  val component = FunctionalComponent[Unit] { _ =>
    val state = Circuit.state(zoomTo(_.scheduleState))
    val dataState = Circuit.state(zoomTo(_.dataSourceListState))
    val flowState = Circuit.state(zoomTo(_.flowListState))
    val drawerRef = React.createRef[Drawer.Def]

    val green = "#26a69a"
    val red = "rgb(184,65,102)"
    val orange = "rgb(207,144,58)"
    val grey = "rgb(200,200,200)"

    Fragment(
      Drawer.withRef(drawerRef),
      GridPage(
        entityType = "schedules",
        state = state,
        title = i"heading.section.schedules",
        itemMenuItems = Some((item: GridPageItem[_]) =>
          List(
            MenuItem(i"apps.menu.stop",
              iconPrefix = Some("lindua", "repeat"),
              onClick = None)
            )
        ),
        tableColumns = List(titleColumn, tagsColumn, historyColumn),
        tableContent = (column: Column, item: GridPageItem[_]) => {
          val schedule = item.entity.asInstanceOf[Schedule]
          val execution = schedule.executions.lastOption

          column match {
            case `titleColumn`    => div(schedule.title)
            case `tagsColumn`     => TagsColumn(Set("confidential", "engineering"))
            case `historyColumn`  =>
              val executions = item.additionalData("recentExecutions").asInstanceOf[List[ScheduleExecution]]
              div(
                executions.map { execution =>
                  val color = execution.status match {
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

                  Tooltip(content = DateUtils.format(execution.started), showDelay = Some(0.1), className = Some("schedules-pill-tooltip"))(
                    List(div(className := "schedules-pill", style := literal("backgroundColor" -> color), onClick := Some(() => dialogs.execution(drawerRef, schedule, executions, execution))))
                  )
                },
                Range(executions.size, 40).map { _ =>
                  div(className := "schedules-pill", style := literal("backgroundColor" -> grey))
                }

              )
          }
        },
        rightSidebarSections = List(
          SidebarSection(
            Some(i"schedules.sidebar.history"),
            className = Some("schedules-history"),
            content =
              div(className := "items")(
                state.additionalState.history.map(pair =>
                  div(className := "item")(
                    statusIcon(pair._1.status),
                    div(className := "title") (pair._2.title),
                    div(className := "info") (PrettyDate(pair._2.created))
                  )
                )
              )
          )
        ),
        editWidth = Some("500px"),
        editAdditionalSections = List(
          (i"schedules.groups.events", div(className := "schedule-editor")(
            table(tbody(state.additionalState.itemEvents.zipWithIndex.map { case (a, index) =>
              EventRow(index, Some(a._2), dataState.entities, flowState.entities, state.entities, allowDelete = state.additionalState.itemEvents.size > 1) }))
            )
          ),
          (i"schedules.groups.actions", div(className := "schedule-editor")(
            table(tbody(state.additionalState.itemActions.zipWithIndex.map { case (a, index) =>
              ActionRow(index, Some(a._2), dataState.entities, flowState.entities, state.entities, allowDelete = state.additionalState.itemActions.size > 1) }))
            )
          )
        )
      )
    )
  }
}