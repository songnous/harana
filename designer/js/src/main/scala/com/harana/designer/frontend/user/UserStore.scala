package com.harana.designer.frontend.user

import com.harana.sdk.shared.models.common.UserSettings
import diode.{Action => DiodeAction}

object UserStore {

  case class UserState(preferences: Map[String, String],
                       preferencesDirty: Boolean,
                       settings: Option[UserSettings] = None,
                       settingsDirty: Boolean)

  val initialState = UserState(Map(), false, None, false)

  case class Init(userPreferences: Map[String, String]) extends DiodeAction

  case class SetPreference(id: String, value: Option[String]) extends DiodeAction

  case object SavePreferences extends DiodeAction
  case object SaveSettings extends DiodeAction

  case class UpdatePreferences(preferences: Map[String, String]) extends DiodeAction
  case class UpdateSettings(settings: Option[UserSettings]) extends DiodeAction
}