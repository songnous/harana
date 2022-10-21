package com.harana.ui.external.terminal_ui

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-terminal-ui", JSImport.Default)
@js.native
object ReactTerminalUI extends js.Object {
  def clear(): Unit = js.native
}

@react object TerminalUI extends ExternalComponent {

  case class Props(name: Option[String] = None,
                   colorMode: Option[String] = None,
                   onInput: Option[(js.Object) => Unit] = None,
                   startingInputValue: Option[String] = None,
                   prompt: Option[String] = None)

  override val component = ReactTerminalUI
}