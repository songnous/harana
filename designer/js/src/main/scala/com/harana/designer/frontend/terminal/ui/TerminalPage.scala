package com.harana.designer.frontend.terminal.ui

import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.navigation.ui.Navigation
import com.harana.designer.frontend.terminal.TerminalStore.{ConnectTerminal, State}
import com.harana.designer.frontend.{Circuit, EventBus}
import com.harana.ui.components.elements.Page
import com.harana.ui.external.xterm.{TerminalOptions, XTerm}
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.Hooks.useEffect
import slinky.core.facade.ReactElement
import slinky.web.html.div

import scala.scalajs.js

@react object TerminalPage {

  val component = FunctionalComponent[js.Dynamic] { props =>
    val state = Circuit.state(zoomTo(_.terminalState))
    val title = "Terminal"

    useEffect(() => {
      val terminal = state.selectedTerminalRef.current
      if (terminal != null) terminal.terminal.focus()
    })

    Page(
      title = title,
      navigationBar = Some(Navigation(())),
      toolbarItems =
        if (state.terminalConnected)
          List(
            toolbar.navigation,
            toolbar.copyPaste,
            toolbar.options
          )
        else List(),
      content = pageContent(state),
      sidebar = None
    )
  }

  def pageContent(state: State): ReactElement =
    if (state.selectedTerminal.isDefined) {
      val id = state.selectedTerminal.get.id

      Circuit.dispatch(ConnectTerminal)

      val terminalId = state.selectedTerminal.get.id

      XTerm(
        options = Some(new TerminalOptions {
          override val allowProposedApi = true
          override val cursorBlink = true
        }),
        onData = Some(data => if (!state.loadingTerminalHistory) EventBus.sendMessage(s"terminal-$terminalId-stdin", data)),
//        onLineFeed = Some(_ => handleLineFeed(terminalId))
      ).withRef(state.selectedTerminalRef)
    } else
      div("Terminal not available")

//  private def handleLineFeed(terminalId: String): Unit = {
//    val buffer = xtermRef.current.terminal.buffer.active
//    val newLine = buffer.getLine(buffer.baseY + buffer.cursorY)
//    if (newLine != null && !newLine.isWrapped) {
//      var lineIndex = buffer.baseY + buffer.cursorY - 1
//      var line = buffer.getLine(lineIndex)
//      if (line == null) return
//
//      var lineData = line.translateToString(Some(true))
//      breakable {
//        while (lineIndex > 0 && line.isWrapped) {
//          lineIndex -= 1
//          line = buffer.getLine(lineIndex)
//          if (line == null) break()
//          lineData = line.translateToString(Some(false)) + lineData
//        }
//      }
//      //EventBus.sendMessage(s"terminal-$terminalId-stdin", lineData + "\n")
//    }
 }