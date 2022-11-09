package com.harana.designer.frontend.terminal.ui

import com.harana.designer.frontend.{Circuit, EventBus, Main}
import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.navigation.ui.Navigation
import com.harana.designer.frontend.terminal.TerminalStore.{ConnectTerminal, TerminalState}
import com.harana.ui.components.elements.Page
import com.harana.ui.external.xterm.{TerminalOptions, XTerm}
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.{React, ReactElement}
import slinky.web.html.div
import typings.std.global.console

import java.util.Base64
import scala.scalajs.js
import scala.util.control.Breaks._

@react object TerminalPage {

  val xtermRef = React.createRef[XTerm.RefType]

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

  def pageContent(state: TerminalState): ReactElement =
    if (state.selectedTerminal.isDefined) {
      val id = state.selectedTerminal.get.id

      Circuit.dispatch(ConnectTerminal)

      EventBus.subscribe(s"terminal-$id-stdout", message => {
        println("STDOUT: " + message)
        xtermRef.current.terminal.write(message)
      })

      EventBus.subscribe(s"terminal-$id-stderr", message => {
        println("STDERR: " + message)
        xtermRef.current.terminal.write(message)
      })

      val terminalId = state.selectedTerminal.get.id

      XTerm(
        options = Some(new TerminalOptions {
          override val allowProposedApi = true
          override val cursorBlink = true
        }),
        onData = Some(data => EventBus.sendMessage(s"terminal-$terminalId-stdin", data)),
//        onKey = Some(event =>
//          event.domEvent.get.keyCode match {
//          case 8 => xtermRef.current.terminal.write("\b \b")
//          case 9 => xtermRef.current.terminal.write("\t")
//          case 13 => xtermRef.current.terminal.write("\r\n")
//          case _ => xtermRef.current.terminal.write(event.key.get)
//         }),
//        ),
        onLineFeed = Some(_ => handleLineFeed(terminalId))
      ).withRef(xtermRef)
    } else
      div("Terminal not available")

  private def handleLineFeed(terminalId: String): Unit = {
    val buffer = xtermRef.current.terminal.buffer.active
    val newLine = buffer.getLine(buffer.baseY + buffer.cursorY)
    if (newLine != null && !newLine.isWrapped) {
      var lineIndex = buffer.baseY + buffer.cursorY - 1
      var line = buffer.getLine(lineIndex)
      if (line == null) return

      var lineData = line.translateToString(Some(true))
      breakable {
        while (lineIndex > 0 && line.isWrapped) {
          lineIndex -= 1
          line = buffer.getLine(lineIndex)
          if (line == null) break
          lineData = line.translateToString(Some(false)) + lineData
        }
      }
      //EventBus.sendMessage(s"terminal-$terminalId-stdin", lineData + "\n")
    }
  }
 }