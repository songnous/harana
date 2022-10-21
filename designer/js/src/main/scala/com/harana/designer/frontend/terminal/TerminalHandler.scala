package com.harana.designer.frontend.terminal

import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.State
import com.harana.designer.frontend.help.HelpStore._
import com.harana.designer.frontend.utils.http.Http
import com.harana.sdk.shared.models.common.HelpCategory
import diode._
import diode.AnyAction._

import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

class TerminalHandler extends ActionHandler(zoomTo(_.terminalState)) {

  override def handle: PartialFunction[Any, ActionResult[State]] = {

    case Init(preferences) =>
      noChange

  }
}
