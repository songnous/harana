package com.harana.designer.frontend.schedules.list.ui.events

import com.harana.sdk.shared.models.schedules.{Event, Schedule}
import com.harana.ui.external.shoelace.{Button, Input, MenuItem, Select}
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.Fragment
import slinky.web.html.{div, table, td, tr}

@react object ScheduleEditor {

  case class Props(rowIndex: Int, event: Event, schedules: List[Schedule])

  val component = FunctionalComponent[Props] { props =>
    Fragment(
      table(
        tr(
          td(
            Select(
              hoist = Some(true),
              name = s"${getClass.getSimpleName}-${props.rowIndex}",
              onChange = Some(id => {}),
              placeholder = Some("Select .."),
              options = List(props.schedules.map(s => MenuItem(s.title, value = Some(s.id)))),
              size = Some("large")
            )
          )
        )
      )
    )
  }
}