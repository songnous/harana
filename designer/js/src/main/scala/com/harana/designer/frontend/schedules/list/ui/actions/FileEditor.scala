package com.harana.designer.frontend.schedules.list.ui.actions

import com.harana.sdk.shared.models.schedules.Action
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.web.html.{div, td}

@react object FileEditor {

  case class Props(rowIndex: Int, action: Action)

  val component = FunctionalComponent[Props] { props =>
    td()
  }
}