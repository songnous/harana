package com.harana.ui.external.terminal

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-terminal", "TerminalContextProvider")
@js.native
object ReactTerminalContextProvider extends js.Object

@react object TerminalContextProvider extends ExternalComponent {

  case class Props(children: ReactElement*)

  override val component = ReactTerminalContextProvider
}