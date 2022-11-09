package com.harana.ui.external.terminal

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-terminal", "ReactTerminal")
@js.native
object ReactTerminal extends js.Object {
  def clear(): Unit = js.native
}

@react object Terminal extends ExternalComponent {

  case class Props(welcomeMessage: Option[String] = None,
                   prompt: Option[String] = None,
                   commands: Option[js.Object] = None,
                   errorMessage: Option[String] = None,
                   enableInput: Option[Boolean] = None,
                   showControlBar: Option[Boolean] = None,
                   showControlButtons: Option[Boolean] = None,
                   theme: Option[String] = None,
                   defaultHandler: Option[String => Unit] = None)

  override val component = ReactTerminal
}