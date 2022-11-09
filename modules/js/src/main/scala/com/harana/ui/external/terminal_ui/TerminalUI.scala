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

@js.native
@JSImport("react-terminal-ui", "TerminalInput")
class TerminalInput(children: String) extends js.Object

@js.native
@JSImport("react-terminal-ui", "TerminalOutput")
class TerminalOutput(children: String) extends js.Object

@react object TerminalUI extends ExternalComponent {

  case class Props(name: Option[String] = None,
                   colorMode: Option[String] = None,
                   onInput: Option[TerminalInput => Unit] = None,
                   startingInputValue: String = "",
                   prompt: String = "$",
                   children: List[TerminalOutput] = List())

  override val component = ReactTerminalUI
}