package com.harana.designer.frontend.terminal

import diode.{Action => DiodeAction}

object TerminalStore {

  case class TerminalState()

  val initialState = TerminalState()

  case class Init(userPreferences: Map[String, String]) extends DiodeAction

}