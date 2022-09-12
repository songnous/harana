package com.harana.designer.frontend.welcome

import diode.{Action => DiodeAction}

object WelcomeStore {

  case class WelcomeState()

  val initialState = WelcomeState()

  case class Init(userPreferences: Map[String, String]) extends DiodeAction
}