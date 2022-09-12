package com.harana.designer.frontend.welcome

import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.{Circuit, Globals, State}
import com.harana.designer.frontend.analytics.Analytics
import com.harana.designer.frontend.welcome.WelcomeStore._
import com.harana.designer.frontend.utils.http.Http
import diode._
import diode.AnyAction._

import scala.concurrent.ExecutionContext.Implicits.global

class WelcomeHandler extends ActionHandler(zoomTo(_.welcomeState)) {

  override def handle: PartialFunction[Any, ActionResult[State]] = {

    case _ =>
      noChange

  }
}