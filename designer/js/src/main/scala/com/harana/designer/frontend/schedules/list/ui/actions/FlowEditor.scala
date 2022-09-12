package com.harana.designer.frontend.schedules.list.ui.actions


import com.harana.sdk.backend.models.designer.flow.Flow
import com.harana.sdk.shared.models.schedules.Action
import com.harana.ui.external.shoelace.{MenuItem, Select}
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.web.html.td

@react object FlowEditor {

  case class Props(rowIndex: Int, action: Action, flows: List[Flow])

  val component = FunctionalComponent[Props] { props =>
    td(
      Select(
        hoist = Some(true),
        name = s"${getClass.getSimpleName}-${props.rowIndex}",
        options = props.flows.map { f => MenuItem(f.title, value=Some(f.title))},
        placeholder = Some("Select .."),
        size = Some("large")
      )
    )
  }
}