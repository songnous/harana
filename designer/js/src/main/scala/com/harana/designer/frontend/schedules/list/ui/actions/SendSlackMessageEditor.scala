package com.harana.designer.frontend.schedules.list.ui.actions

import com.harana.sdk.shared.models.schedules.Action
import com.harana.ui.external.shoelace.Input
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.Fragment
import slinky.web.html.{table, td, tr}

@react object SendSlackMessageEditor {

  case class Props(rowIndex: Int, action: Action.SendSlackMessage)

  val component = FunctionalComponent[Props] { props =>
    Fragment(
      table(
        tr(
          td(
            Input(
              name = s"${getClass.getSimpleName}-${props.rowIndex}",
              placeholder = Some("username"),
              size = Some("large")
            )
          )
        )
      )
    )
  }
}