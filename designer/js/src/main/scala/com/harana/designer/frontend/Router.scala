package com.harana.designer.frontend

import com.harana.designer.frontend.analytics.Analytics
import com.harana.designer.frontend.apps.item.AppItemStore

import java.util.concurrent.atomic.AtomicReference
import com.harana.designer.frontend.apps.item.ui.AppItemPage
import com.harana.designer.frontend.apps.list.ui.AppListPage
import com.harana.designer.frontend.common.grid.GridStore
import com.harana.designer.frontend.data.item.DataSourceItemStore
import com.harana.designer.frontend.data.item.ui.DataSourceItemPage
import com.harana.designer.frontend.data.list.ui.DataSourceListPage
import com.harana.designer.frontend.files.FilesStore
import com.harana.designer.frontend.files.ui.FilesPage
import com.harana.designer.frontend.flows.item.FlowItemStore
import com.harana.designer.frontend.flows.item.ui.FlowItemPage
import com.harana.designer.frontend.flows.list.ui.FlowListPage
import com.harana.designer.frontend.help.HelpStore
import com.harana.designer.frontend.help.ui.HelpPage
import com.harana.designer.frontend.navigation.NavigationStore
import com.harana.designer.frontend.schedules.item.ScheduleItemStore
import com.harana.designer.frontend.schedules.item.ui.ScheduleItemPage
import com.harana.designer.frontend.schedules.list.ui.ScheduleListPage
import com.harana.designer.frontend.system.SystemStore
import com.harana.designer.frontend.welcome.ui.WelcomePage
import com.harana.designer.frontend.terminal.TerminalStore
import com.harana.designer.frontend.terminal.ui.TerminalPage
import com.harana.designer.frontend.test._
import com.harana.designer.frontend.user.UserStore
import com.harana.designer.frontend.utils.http.Http
import com.harana.designer.frontend.welcome.WelcomeStore
import com.harana.ui.external.helmet.Helmet
import com.harana.ui.external.react_router.{Route, Switch, Router => ReactRouter}
import com.harana.ui.external.filepond.ReactFilePondCSS
import diode.{Effect, Circuit => DiodeCircuit}
import slinky.core.annotations.react
import slinky.core.facade.Hooks.useEffect
import slinky.core.facade.React
import slinky.core.{CustomAttribute, FunctionalComponent}
import slinky.web.html._
import com.harana.sdk.shared.utils.CirceCodecs._
import com.harana.ui.external.xterm.XTermCSS
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

@react
object Router {

  type Props = Unit

  val browserHistory = Analytics.history
  val diodeContext = React.createContext[DiodeCircuit[State]](Circuit)
  private val didInit = new AtomicReference[Boolean](false)

  val component = FunctionalComponent[Unit] { _ =>

    diodeContext.Provider(Circuit) {

      useEffect(() => {
        if (!didInit.get)
          Http.getRelativeAs[Map[String, String]](s"/api/user/preferences").map { preferences =>
//            val p = preferences.getOrElse(Map())

            val p = Map.empty[String, String]

            Circuit.dispatch(AppItemStore.Init(p))
            Circuit.dispatch(DataSourceItemStore.Init(p))
            Circuit.dispatch(FilesStore.Init(p))
            Circuit.dispatch(FlowItemStore.Init(p))
            Circuit.dispatch(GridStore.Init(p))
            Circuit.dispatch(HelpStore.Init(p))
            Circuit.dispatch(NavigationStore.Init(p))
            Circuit.dispatch(ScheduleItemStore.Init(p))
            Circuit.dispatch(SystemStore.Init(p))
            Circuit.dispatch(TerminalStore.Init(p))
            Circuit.dispatch(UserStore.Init(p))
            Circuit.dispatch(WelcomeStore.Init(p))

            didInit.set(true)
          }
      })

      ReactRouter(history = browserHistory)(
        div(
          Helmet(
            meta(new CustomAttribute[String]("charSet") := "utf-8"),
            meta(name := "viewport", content := "width=device-width, initial-scale=1, shrink-to-fit=no"),
            meta(name := "theme-color", content := "#000000"),
            link(rel := "manifest", href := "/manifest.json"),
            link(rel := "shortcut icon", href := "/favicon.ico"),
            style(`type` := "text/css")(ReactFilePondCSS.toString),
            style(`type` := "text/css")(XTermCSS.toString),
          ),
          Switch(
             Route("/", ScheduleListPage.component, exact = true),
             Route("/apps", AppListPage.component, exact = true),
             Route("/apps/:id", AppItemPage.component, exact = true),
             Route("/data", DataSourceListPage.component, exact = true),
             Route("/data/:id", DataSourceItemPage.component, exact = true),
             Route("/files", FilesPage.component, exact = true),
             Route("/files*", FilesPage.component, exact = true),
             Route("/flows", FlowListPage.component, exact = true),
             Route("/flows/:id", FlowItemPage.component, exact = true),
             Route("/help", HelpPage.component, exact = true),
             Route("/help/:id", HelpPage.component, exact = true),
             Route("/schedules", ScheduleListPage.component, exact = true),
             Route("/schedules/:id", ScheduleItemPage.component, exact = true),
             Route("/terminal", TerminalPage.component, exact = true),
             Route("/test/parameters", ParametersPage.component, exact = true),
             Route("/test/shoelace", ShoelacePage.component, exact = true),
             Route("/welcome", WelcomePage.component, exact = true),
             Route("*", ScheduleListPage.component)
          )
        )
      )
    }
  }
}
