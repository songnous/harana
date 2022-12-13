package com.harana.designer.frontend.schedules.list.ui.actions

import com.harana.sdk.shared.models.schedules.Action
import com.harana.ui.external.shoelace.Select
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.Fragment
import slinky.web.html._

@react object ExecuteCommandEditor {

  case class Props(rowIndex: Int, action: Action)

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
              size = Some("large")
            )
          )
        )
      )
    )
  }
}