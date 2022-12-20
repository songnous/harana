package com.harana.designer.frontend.schedules.ui.actions

import com.harana.sdk.shared.models.data.DataSource
import com.harana.sdk.shared.models.schedules.Action
import com.harana.ui.external.shoelace.{MenuItem, Select}
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.Fragment
import slinky.web.html._

@react object DataSyncEditor {

  case class Props(rowIndex: Int, action: Action.DataSync, datasources: List[DataSource])

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
              options = List(props.datasources.map(ds => MenuItem(ds.title, value = Some(ds.id)))),
              size = Some("large")
            )
          )
        )
      )
    )
  }
}