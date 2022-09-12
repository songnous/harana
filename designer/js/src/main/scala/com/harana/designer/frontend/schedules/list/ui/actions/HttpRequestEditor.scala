package com.harana.designer.frontend.schedules.list.ui.actions

import com.harana.sdk.shared.models.schedules.Action
import com.harana.ui.external.shoelace.{Button, Input}
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.Fragment
import slinky.web.html.td

@react object HttpRequestEditor {

  case class Props(rowIndex: Int, action: Action.HttpRequest)

  val component = FunctionalComponent[Props] { props =>
    Fragment(
      td(
        Input(
          name = s"${getClass.getSimpleName}-${props.rowIndex}",
          placeholder = Some("https://"),
          size = Some("large")
        )
      ),
      td(
        Button(label = Some("options .."))
      )
    )
  }
}