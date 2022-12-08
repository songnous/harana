package com.harana.designer.frontend.terminal

import com.harana.designer.frontend.EventBus
import com.harana.sdk.shared.models.terminals.Terminal
import com.harana.ui.external.xterm.{Terminal => XTerminal}
import diode.{Action => DiodeAction}

object TerminalStore {

  case class State(loadingTerminalHistory: Boolean,
                   selectedTerminal: Option[Terminal],
                   terminalConnected: Boolean,
                   xTerminal: Option[XTerminal],
                   terminals: List[Terminal])

  val initialState = State(false, None, false, None, List())

  case object Nothing extends DiodeAction
  case class Init(userPreferences: Map[String, String]) extends DiodeAction

  case class NewTerminal(terminal: Terminal) extends DiodeAction
  case object DeleteTerminal extends DiodeAction
  case object ConnectTerminal extends DiodeAction
  case object DisconnectTerminal extends DiodeAction
  case object RestartTerminal extends DiodeAction
  case object ClearTerminal extends DiodeAction
  case object RefreshTerminal extends DiodeAction

  case object ScrollTerminalToTop extends DiodeAction
  case object ScrollTerminalToBottom extends DiodeAction
  case object CopyFromTerminal extends DiodeAction
  case object PasteToTerminal extends DiodeAction

  case class SelectTerminal(terminal: Terminal) extends DiodeAction

  case class UpdateLoadingTerminalHistory(loading: Boolean) extends DiodeAction
  case class UpdateSelectedTerminal(terminal: Option[Terminal]) extends DiodeAction
  case class UpdateTerminals(terminals: List[Terminal]) extends DiodeAction
  case class UpdateTerminalConnected(terminalConnected: Boolean) extends DiodeAction
  case class UpdateXTerminal(terminal: Option[XTerminal]) extends DiodeAction

}