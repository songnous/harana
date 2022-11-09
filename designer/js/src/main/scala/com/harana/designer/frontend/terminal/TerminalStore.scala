package com.harana.designer.frontend.terminal

import com.harana.sdk.shared.models.terminals.{Terminal, TerminalHistory}
import diode.{Action => DiodeAction}

import scala.collection.mutable.ListBuffer

object TerminalStore {

  case class TerminalState(selectedTerminal: Option[Terminal],
                           selectedTerminalHistory: ListBuffer[TerminalHistory],
                           terminals: List[Terminal])

  val initialState = TerminalState(None, ListBuffer.empty, List())

  case object Nothing extends DiodeAction
  case class Init(userPreferences: Map[String, String]) extends DiodeAction

  case class NewTerminal(terminal: Terminal) extends DiodeAction
  case object DeleteTerminal extends DiodeAction
  case object ConnectTerminal extends DiodeAction
  case object DisconnectTerminal extends DiodeAction
  case object RestartTerminal extends DiodeAction

  case class SelectTerminal(terminal: Terminal) extends DiodeAction
  case class AddToTerminalHistory(message: TerminalHistory) extends DiodeAction

  case class UpdateSelectedTerminal(terminal: Option[Terminal]) extends DiodeAction
  case class UpdateTerminals(terminals: List[Terminal]) extends DiodeAction

}