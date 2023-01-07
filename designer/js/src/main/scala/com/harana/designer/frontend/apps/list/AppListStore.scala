package com.harana.designer.frontend.apps.list

import diode.{Action => DiodeAction}

object AppListStore {

  case class State(latestVersions: Map[String, String])

  val initialState = State(Map())

  case class StopApp(appId: String) extends DiodeAction
  case class UpdateLatestVersions(versions: Map[String, String]) extends DiodeAction
}