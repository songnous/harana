package com.harana.designer.frontend.welcome

import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.State
import diode._

class WelcomeHandler extends ActionHandler(zoomTo(_.welcomeState)) {

  override def handle: PartialFunction[Any, ActionResult[State]] = {

    case _ =>
      noChange

  }
}