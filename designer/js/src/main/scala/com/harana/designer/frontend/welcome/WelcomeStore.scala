package com.harana.designer.frontend.welcome

import diode.{Action => DiodeAction}

object WelcomeStore {

  case class State()

  val initialState = State()

  case class Init(userPreferences: Map[String, String]) extends DiodeAction

}