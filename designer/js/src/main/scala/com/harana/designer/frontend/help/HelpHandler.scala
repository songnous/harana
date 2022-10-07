package com.harana.designer.frontend.help

import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.State
import com.harana.designer.frontend.help.HelpStore._
import com.harana.designer.frontend.utils.http.Http
import com.harana.sdk.shared.models.common.HelpCategory
import diode._
import diode.AnyAction._

import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

class HelpHandler extends ActionHandler(zoomTo(_.helpState)) {

  override def handle: PartialFunction[Any, ActionResult[State]] = {

    case Init(preferences) =>
      effectOnly(
        Effect(Http.getRelativeAs[List[HelpCategory]](s"/api/help").map { categories =>
          updated(value.copy(categories = categories.getOrElse(List())))
        })
      )
  }
}
