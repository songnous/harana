package com.harana.designer.frontend.user

import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.system.SystemStore._
import com.harana.designer.frontend.user.UserStore.{SavePreferences, SaveSettings, SetPreference, UpdatePreferences, UpdateSettings}
import com.harana.designer.frontend.utils.http.Http
import com.harana.designer.frontend.{Circuit, State}
import com.harana.sdk.shared.models.common.UserSettings
import diode._
import io.circe.syntax._
import com.harana.sdk.shared.utils.CirceCodecs._

import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

class UserHandler extends ActionHandler(zoomTo(_.userState)) {
  override def handle = {

    case Init(preferences) =>
      effectOnly(
        Effect(Http.getRelativeAs[Map[String, String]](s"/api/user/preferences").map(p => UpdatePreferences(p.getOrElse(Map())))) +
        Effect(Http.getRelativeAs[UserSettings](s"/api/user/settings").map(s => UpdateSettings(s)))
      )


    case SetPreference(id, updatedValue) =>
      val updatedPreferences = updatedValue match {
        case Some(v) => value.preferences + (id -> v)
        case None => value.preferences - id
      }

      val dirty = (value.preferences.get(id), updatedPreferences.get(id)) match {
        case (Some(existingValue), Some(newValue)) => existingValue != newValue
        case _ => true
      }

      updatedSilent(value.copy(preferences = updatedPreferences, preferencesDirty = dirty))


    case SavePreferences =>
      if (value.preferencesDirty)
        updatedSilent(value.copy(preferencesDirty = false), Effect(Http.postRelative(s"/api/user/preferences", body = value.preferences.asJson.noSpaces).map(_ => NoAction)))
      else
        noChange


    case SaveSettings =>
      if (value.settings.isDefined)
        effectOnly(Effect(Http.postRelative(s"/api/user/settings", body = value.settings.get.asJson.noSpaces).map(_ => NoAction)))
      else
        noChange

    case UpdatePreferences(preferences) =>
      updated(value.copy(preferences = preferences))


    case UpdateSettings(settings) =>
      updated(value.copy(settings = settings))
  }
}