package com.harana.designer.frontend.apps.item

import com.harana.sdk.shared.models.apps.App
import App.AppId
import diode.{Action => DiodeAction}

object AppItemStore {

  case class AppItemState(app: Option[App], appLaunching: Boolean)

  val initialState = AppItemState(None, false)


  case class Init(userPreferences: Map[String, String]) extends DiodeAction
  case class StartApp(id: AppId) extends DiodeAction
  case class StopApp(id: AppId) extends DiodeAction
  case class UpdateApp(app: Option[App]) extends DiodeAction
  
  case object AppLaunched extends DiodeAction
}