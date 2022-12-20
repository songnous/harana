package com.harana.designer.frontend.schedules.ui

import com.harana.designer.frontend.utils.DateUtils
import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.sdk.shared.models.schedules.{Schedule, ScheduleExecution, ScheduleExecutionStatus}
import com.harana.ui.components.elements.{Drawer, DrawerStyle}
import com.harana.ui.external.lazy_log.LazyLog
import com.harana.ui.external.shoelace.{MenuItem, Select}
import slinky.core.facade.ReactRef
import slinky.web.html._
import org.scalajs.dom.window

object dialogs {

  def execution(ref: ReactRef[Drawer.Def],
                schedule: Schedule,
                executions: List[ScheduleExecution],
                selectedExecution: ScheduleExecution): Unit =
    ref.current.show(
      title = Some(i"files.menu.new.new-folder"),
      width = Some("80%"),
      style = DrawerStyle.General(
        table(className := "schedules-execution-table")(
          tr(
            td(className := "schedules-execution-main")(
              h4(className := "schedules-execution-title")(schedule.title, statusIcon(selectedExecution.status, Some("schedules-heading-circle-icon"))),
              div(className := "schedules-execution-logs")(
                LazyLog(
                  url = "/api/test/logs",
                  lineClassName = "schedules-execution-logs-line",
                  enableSearch = true,
                  caseInsensitive = true,
                  stream = true,
                  height = window.screen.height
                )
              )
            ),
            td(className := "schedules-execution-sidebar")(
              div(className := "schedules-execution-sidebar")(
                Select(
                  className = Some("schedules-execution-select"),
                  name = "executions",
                  options = executions.sortBy(_.started).map(e => MenuItem(DateUtils.format(e.started), value = Some(e.id))),
                  size = Some("large"),
                  value = Some(selectedExecution.id)
                ),
                div(className := "schedules-circle-section")(
                  h4(className := "schedules-circle-heading")("Events"),
                  schedule.events.map(e => {
                    div(className := "schedules-execution-row")(
                      span(className := "schedules-circle"),
                      span(className := "schedules-circle-text")(i"schedules.events.${e._2.getClass.getSimpleName.toLowerCase}")
                    )
                  }),
                  div(className := "schedules-execution-row")(
                    statusIcon(ScheduleExecutionStatus.Succeeded, Some("schedules-circle-icon")),
                    span(className := "schedules-circle-text")("Success")
                  ),
                  div(className := "schedules-execution-row")(
                    statusIcon(ScheduleExecutionStatus.Failed, Some("schedules-circle-icon")),
                    span(className := "schedules-circle-text")("Failed")
                  )
                ),
                div(className := "schedules-circle-section")(
                  h4(className := "schedules-circle-heading")("Actions"),
                  schedule.actions.map(a => {
                    div(className := "schedules-execution-row")(
                      span(className := "schedules-circle"),
                      span(className := "schedules-circle-text")(i"schedules.actions.${a._2.getClass.getSimpleName.toLowerCase}")
                    )
                  })
                )
              )
            )
          )
        ),
        okButtonLabel = "OK"
      ),
      values = None,
    )
}