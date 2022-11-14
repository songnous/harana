package com.harana.designer.frontend.help

import com.harana.sdk.shared.models.common.{HelpCategory}
import diode.{Action => DiodeAction}

object HelpStore {

  case class State(categories: List[HelpCategory])

  val initialState = State(List())

  case class Init(userPreferences: Map[String, String]) extends DiodeAction

}