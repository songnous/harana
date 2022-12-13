package com.harana.designer.frontend.schedules.list.ui.actions

import com.harana.sdk.shared.models.schedules.Action
import com.harana.ui.external.shoelace.Input
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.Fragment
import slinky.web.html.{table, td, tr}

@react object SendEmailEditor {

  case class Props(rowIndex: Int, action: Action.SendEmail)

  val component = FunctionalComponent[Props] { props =>
    Fragment(
      table(
        tr(
          td(
            Input(
              name = s"${getClass.getSimpleName}-${props.rowIndex}",
              placeholder = Some("email@domain.com"),
              size = Some("large")
            )
          )
        )
      )
    )
  }
}