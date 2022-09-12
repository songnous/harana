package com.harana.designer.frontend.system

import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.Globals
import com.harana.designer.frontend.analytics.Analytics
import com.harana.designer.frontend.apps.item.AppItemStore
import com.harana.designer.frontend.common.grid.GridStore
import com.harana.designer.frontend.system.SystemStore._
import com.harana.designer.frontend.files.FilesStore.{ReceiveEvent => FilesReceiveEvent}
import com.harana.designer.frontend.common.grid.GridStore.{ReceiveEvent => GridReceiveEvent}
import com.harana.designer.frontend.data.item.DataSourceItemStore
import com.harana.designer.frontend.files.FilesStore
import com.harana.designer.frontend.flows.item.FlowItemStore
import com.harana.designer.frontend.help.HelpStore
import com.harana.designer.frontend.navigation.NavigationStore
import com.harana.designer.frontend.schedules.item.ScheduleItemStore
import com.harana.designer.frontend.user.UserStore
import com.harana.designer.frontend.utils.error.Error
import com.harana.designer.frontend.utils.http.Http
import com.harana.designer.frontend.welcome.WelcomeStore
import com.harana.designer.frontend.{Circuit, State}
import com.harana.sdk.shared.models.common.Event
import diode.AnyAction.aType
import diode._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class SystemHandler extends ActionHandler(zoomTo(_.systemState)) {
  override def handle: PartialFunction[Any, ActionResult[State]] = {

    case InitSourceMaps =>
      effectOnly(
        Effect(Error.init(List(Globals.jsBundle)).map(_ => NoAction))
      )


    case ClearSourceMaps =>
      Error.clear
      noChange


    case RefreshEvents =>
      effectOnly(
        Effect(Http.getRelativeAs[List[Event]](s"/api/events").map(events => UpdateEvents(events.getOrElse(List.empty))))
      )


    case UpdateEvents(events) =>
      updated(value.copy(events = events), Effect(
        Future {
          events.foreach { e =>
            Analytics.receiveEvent(e.eventType, e.eventParameters)
            Circuit.dispatch(FilesReceiveEvent(e.eventType, e.eventParameters))
            Circuit.dispatch(GridReceiveEvent("apps", e.eventType, e.eventParameters))
            Circuit.dispatch(GridReceiveEvent("datasources", e.eventType, e.eventParameters))
            Circuit.dispatch(GridReceiveEvent("flows", e.eventType, e.eventParameters))
            Circuit.dispatch(DeleteEvent(e))
          }
        }).map(_ => NoAction)
      )


    case DeleteEvent(event) =>
      effectOnly(
        Effect(Http.deleteRelative(s"/api/events/${event.id}"))
      )


    case ToggleDebug =>
      updated(value.copy(debug = !value.debug), Effect.action(if (value.debug) ClearSourceMaps else InitSourceMaps))
  }
}