package com.harana.designer.frontend.terminal.ui

import com.harana.designer.frontend.Circuit
import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.help.HelpStore.HelpState
import com.harana.designer.frontend.navigation.ui.Navigation
import com.harana.designer.frontend.terminal.TerminalStore.TerminalState
import com.harana.ui.components.elements.Page
import com.harana.ui.components.sidebar.{NavigationGroup, NavigationItem, NavigationSection, Sidebar, SidebarSection}
import com.harana.ui.external.terminal.{Terminal, TerminalContextProvider}
import com.harana.ui.external.terminal_ui.TerminalUI
import slinky.core.{CustomTag, FunctionalComponent}
import slinky.core.annotations.react
import slinky.web.html._

import scala.scalajs.js

@react object TerminalPage {

  val component = FunctionalComponent[js.Dynamic] { props =>
    val state = Circuit.state(zoomTo(_.terminalState))
    val title = "Terminal"

    Page(
      title = title,
      navigationBar = Some(Navigation(())),
      content = pageContent(state),
      sidebar = None
    )
  }

  def pageContent(state: TerminalState) = {
    TerminalContextProvider(
      Terminal()
      //Terminal(showControlBar = Some(true), showControlButtons = Some(true))
    )
    //    TerminalUI()(
//      CustomTag("TerminalOutput")(
//        "Hi"
//      )
//    )
  }
}
