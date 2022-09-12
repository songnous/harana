package com.harana.designer.frontend.apps.item

import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.analytics.Analytics
import com.harana.designer.frontend.apps.item.AppItemStore._
import com.harana.designer.frontend.apps.item.ui.AppItemPage
import com.harana.designer.frontend.utils.http.Http
import com.harana.designer.frontend.{Circuit, State}
import com.harana.sdk.shared.models.apps.App
import diode._

import scala.concurrent.ExecutionContext.Implicits.global

class AppItemHandler extends ActionHandler(zoomTo(_.appItemState)) {
  override def handle: PartialFunction[Any, ActionResult[State]] = {

    case Init(preferences) =>
      noChange


    case StartApp(id) =>
      Analytics.appStart(id, value.app.map(_.title).getOrElse(""))

      updated(value.copy(app = None, appLaunching = true),
        Effect(Http.getRelativeAs[App](s"/api/apps/start/$id").map(app => UpdateApp(app)))
      )
    
    
    case StopApp(id) =>
      Analytics.appStop(id, value.app.map(_.title).getOrElse(""))

      // Hack
      AppItemPage.currentApp.set("")

      updated(value.copy(app = None),
        Effect(Http.getRelative(s"/api/apps/stop/$id").map(_ => UpdateApp(None)))
      )


    case UpdateApp(app) =>
      updated(value.copy(app = app))


    case AppLaunched =>
      updated(value.copy(appLaunching = false))
  }
}