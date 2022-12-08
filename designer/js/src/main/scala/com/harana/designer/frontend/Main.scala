package com.harana.designer.frontend

import com.harana.designer.frontend.analytics.Analytics
import com.harana.designer.frontend.navigation.NavigationStore.SaveRoute
import com.harana.designer.frontend.utils.AuthUtils
import com.harana.designer.frontend.utils.error.Error
import com.harana.designer.frontend.utils.http.Http
import com.harana.sdk.shared.models.jwt.DesignerClaims
import diode._
import org.scalajs.dom
import org.scalajs.dom.HashChangeEvent
import slinky.hot
import slinky.web.ReactDOM

import java.util.Timer
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel, JSGlobalScope}
import scala.scalajs.{LinkingInfo, js}

@JSExportTopLevel("Main")
object Main {
  var claims: DesignerClaims = _

  @JSExport
  def main(args: Array[String]): Unit = {

    Analytics.init();
    Analytics.session();
    EventBus.init();

    val decodedJwt = AuthUtils.decode(Globals.initialJwt)
    if (decodedJwt.isEmpty) println("Failed to decode JWT")
    else claims = decodedJwt.get

    js.Dynamic.global.window.onerror = Error.fromJS

    Http.getRelativeAs[Unit](s"/api/schedules/setup")


    Circuit.addProcessor((dispatch: Dispatcher, action: Any, next: Any => ActionResult[State], currentModel: State) => {
      val actions = action match {
        case a: ActionBatch => a.actions
        case x => List(x)
      }
      //actions.foreach(a => println(s"Action: ${a.getClass.getName.replaceFirst("\\$", " -> ").replace("$", "")}"))
      next(action)
    })

    if (LinkingInfo.developmentMode) hot.initialize()

    val container = Option(dom.document.getElementById("root")).getOrElse {
      val elem = dom.document.createElement("div")
      elem.id = "root"
      dom.document.body.appendChild(elem)
      elem
    }

    dom.window.onhashchange = (e: HashChangeEvent) => {
      val hash = dom.window.location.hash
      if (hash.nonEmpty) {}
    }

    ReactDOM.render(Router(), container)

    new Timer().scheduleAtFixedRate(new java.util.TimerTask {
      def run(): Unit = {
//        Circuit.dispatch(RefreshEvents)
        Circuit.dispatch(SaveRoute)
//        Circuit.dispatch(SavePreferences)
      }
    }, 0L, 2000L)
  }
}

@js.native
@JSGlobalScope
object Globals extends js.Object {
  var authDomain: String = js.native
  var debug: Boolean = js.native
  var domain: String = js.native
  var gaMeasurementId: String = js.native
  var initialJwt: String = js.native
  var i18n: String = js.native
  var jsBundle: String = js.native
  var proxyDomain: String = js.native
}