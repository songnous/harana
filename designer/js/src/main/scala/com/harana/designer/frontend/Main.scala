package com.harana.designer.frontend

import com.harana.designer.frontend.analytics.Analytics
import com.harana.designer.frontend.utils.AuthUtils
import com.harana.designer.frontend.utils.error.Error
import com.harana.sdk.shared.models.jwt.DesignerClaims
import diode._
import org.scalajs.dom
import org.scalajs.dom.HashChangeEvent
import slinky.hot
import slinky.web.ReactDOM
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import java.util.Timer
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel, JSGlobalScope}
import scala.scalajs.{LinkingInfo, js}

@JSExportTopLevel("Main")
object Main {
  var claims: DesignerClaims = _

  @JSExport
  def main(args: Array[String]): Unit = {
    val decodedJwt = AuthUtils.decode(Globals.initialJwt)
    if (decodedJwt.isEmpty) println("Failed to decode JWT")
    else claims = decodedJwt.get

    Analytics.init();
    Analytics.session();

    js.Dynamic.global.window.onerror = Error.fromJS
//    val eventBus = EventBus.eventBus

    Circuit.addProcessor((dispatch: Dispatcher, action: Any, next: Any => ActionResult[State], currentModel: State) => {
      //println(s"Action: ${action.getClass.getName.replaceFirst("\\$", " -> ").replace("$", "")}")
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
//        Circuit.dispatch(SaveRoute)
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