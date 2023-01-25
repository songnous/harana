package com.harana.designer.frontend.apps.item.ui

import com.harana.designer.frontend.Globals
import com.harana.designer.frontend.apps.item.AppItemStore._
import com.harana.designer.frontend.Circuit
import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.navigation.ui.Navigation
import com.harana.ui.external.shoelace.ProgressBar
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.Hooks.useEffect
import slinky.core.facade.ReactElement
import slinky.web.html._
import com.harana.ui.components.elements.Page

import java.util.concurrent.atomic.AtomicReference
import scala.scalajs.js

@react object AppItemPage {
  
  val currentApp = new AtomicReference[String]("")

  val component = FunctionalComponent[js.Dynamic] { props =>
    val state = Circuit.state(zoomTo(_.appItemState))

    useEffect(() => {
      val appId = props.selectDynamic("match").params.selectDynamic("id").toString
      if (!currentApp.get.equals(appId)) {
        Circuit.dispatch(StartApp(appId))
        currentApp.set(appId)
      }
    })

    def pageContent: ReactElement =
      div(className := "app-progressbar-content")(
        if (state.app.nonEmpty)
          iframe(
            className := s"full-screen-iframe ${if (state.appLaunching) "hidden" else "visible"}",
            src := s"https://${Globals.proxyDomain}",
            onLoad := Some(() => Circuit.dispatch(AppLaunched))
          )
        else
          ProgressBar(
            className = Some("apps-progressbar"),
            indeterminate = Some(true)
          )
      )

    Page(
      title = state.app.map(_.title).getOrElse(""),
      fullSizedContent = true,
      navigationBar = Some(Navigation(())),
      content = pageContent
    )
  }
}