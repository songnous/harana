package com.harana.designer.frontend.schedules.ui.actions

import com.harana.sdk.shared.models.flow.Flow
import com.harana.sdk.shared.models.schedules.Action
import com.harana.ui.external.shoelace.{MenuItem, Select}
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.web.html.{table, td, tr}

@react object FlowEditor {

  case class Props(rowIndex: Int, action: Action, flows: List[Flow])

  val component = FunctionalComponent[Props] { props =>
    table(
      tr(
        td(
          Select(
            hoist = Some(true),
            name = s"${getClass.getSimpleName}-${props.rowIndex}",
            options = props.flows.map { f => MenuItem(f.title, value = Some(f.title)) },
            placeholder = Some("Select .."),
            size = Some("large")
          )
        )
      )
    )
  }
}