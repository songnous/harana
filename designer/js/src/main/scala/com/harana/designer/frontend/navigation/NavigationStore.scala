package com.harana.designer.frontend.navigation

import diode.{Action => DiodeAction}

object NavigationStore {

  case class State(openingCheckout: Boolean)

  val initialState = State(false)

  case class Init(userPreferences: Map[String, String]) extends DiodeAction
  case class ReceiveEvent(eventType: String, eventParameters: Map[String, String]) extends DiodeAction

  case object OpenCheckout extends DiodeAction
  case class OpenRoute(route: String) extends DiodeAction
  case object SaveRoute extends DiodeAction

  case class UpdateOpeningCheckout(opening: Boolean) extends DiodeAction
}