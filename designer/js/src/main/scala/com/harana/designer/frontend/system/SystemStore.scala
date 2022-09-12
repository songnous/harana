package com.harana.designer.frontend.system

import com.harana.designer.frontend.Globals
import com.harana.sdk.shared.models.common.Event
import diode.{Action => DiodeAction}

object SystemStore {

  case class SystemState(debug: Boolean,
                         events: List[Event])

  val initialState = SystemState(Globals.debug, List())

  case class Init(userPreferences: Map[String, String]) extends DiodeAction

  case object InitSourceMaps extends DiodeAction
  case object ClearSourceMaps extends DiodeAction

  case object RefreshEvents extends DiodeAction
  case class DeleteEvent(events: Event) extends DiodeAction
  case class UpdateEvents(events: List[Event]) extends DiodeAction

  case object ToggleDebug extends DiodeAction
}