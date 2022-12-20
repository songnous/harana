package com.harana.designer.frontend.terminal.ui

import com.harana.designer.frontend.Circuit
import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.navigation.ui.Navigation
import com.harana.designer.frontend.terminal.TerminalStore.{ConnectTerminal, State}
import com.harana.ui.components.elements.Page
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.Hooks.useEffect
import slinky.core.facade.ReactElement
import slinky.web.html._

import scala.scalajs.js

@react object TerminalPage {

  val component = FunctionalComponent[js.Dynamic] { props =>
    val state = Circuit.state(zoomTo(_.terminalState))
    val title = "Terminal"

    useEffect(() => {
      if (state.selectedTerminal.isDefined && state.xTerminal.isEmpty)
        Circuit.dispatch(ConnectTerminal)

        if (state.xTerminal.isDefined)
          state.xTerminal.get.focus()
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
      content = pageContent(state)
    )
  }

  def pageContent(state: State): ReactElement =
    div(id := "terminal-container")
}

      //  private def handleLineFeed(terminalId: String): Unit = {
//    val buffer = xtermRef.current.terminal.buffer.active
//    val newLine = buffer.geetLine(buffer.baseY + buffer.cursorY)
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
