package com.harana.designer.frontend.schedules.ui.actions

import com.harana.sdk.shared.models.schedules.Action
import com.harana.ui.external.shoelace.{Button, Select}
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.Fragment
import slinky.web.html._

@react object ExecuteScriptEditor {

  case class Props(rowIndex: Int, action: Action)

  val component = FunctionalComponent[Props] { props =>
    Fragment(
      table(
        tr(
          td(
            Button(
              label = Some("message .."),
              size = Some("large")
            )
          )
        )
      )
    )
  }
}