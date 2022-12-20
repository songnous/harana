package com.harana.designer.frontend.schedules.ui.common

import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.web.html.div

@react object FileFilterEditor {

  case class Props()

  val component = FunctionalComponent[Unit] { _ =>
    div()
  }
}