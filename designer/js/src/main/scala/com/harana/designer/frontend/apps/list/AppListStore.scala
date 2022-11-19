package com.harana.designer.frontend.apps.list

import diode.{Action => DiodeAction}

object AppListStore {

  case class State()

  val initialState = State()

  case class StopApp(appId: String) extends DiodeAction
}