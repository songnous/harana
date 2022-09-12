package com.harana.designer.frontend.schedules.list.ui.events

import com.harana.sdk.shared.models.schedules.Event
import com.harana.ui.external.shoelace.{Button, Input}
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.Fragment
import slinky.web.html.td

@react object DataSyncEditor {

  case class Props(rowIndex: Int, event: Event)

  val component = FunctionalComponent[Props] { props =>
    Fragment(
      td(
        Input(
          name = s"${getClass.getSimpleName}-${props.rowIndex}",
          placeholder = Some("email@domain.com"),
          size = Some("large")
        )
      ),
      td(
        Button(label = Some("message .."))
      )
    )
  }
}