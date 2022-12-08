package com.harana.designer.frontend.terminal

import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.terminal.TerminalStore._
import com.harana.designer.frontend.utils.http.Http
import com.harana.designer.frontend.{EventBus, Main}
import com.harana.sdk.shared.models.terminals.{Terminal, TerminalHistory}
import com.harana.ui.external.xterm.{FitAddon, Terminal => XTerminal}
import diode.AnyAction._
import diode._
import io.circe.syntax.EncoderOps
import org.scalablytyped.runtime.TopLevel.asT
import org.scalajs.dom.{Event, document, window}
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._
import typings.std.global.navigator

import java.time.{Duration, Instant}
import scala.concurrent.Future

class TerminalHandler extends ActionHandler(zoomTo(_.terminalState)) {

  private var lastResize: Option[Instant] = None

  override def handle = {

    case Init(preferences) =>
      effectOnly(
        Effect(Http.getRelativeAs[List[Terminal]](s"/api/terminals").map(terminals =>
          if (terminals.getOrElse(List()).isEmpty)
            NewTerminal(Terminal("Title", "", Main.claims.userId, "ubuntu", "bash"))
          else
            ActionBatch(
              UpdateTerminals(terminals.get),
              SelectTerminal(terminals.get.head)
            )
        ))
      )


    case NewTerminal(terminal) =>
      effectOnly(
        Effect(Http.postRelativeAs[Terminal](s"/api/terminals", List(), terminal)) >>
          Effect.action(UpdateTerminals(value.terminals :+ terminal)) >>
          Effect.action(SelectTerminal(terminal))
      )


    case DeleteTerminal =>
      effectOnly(
        Effect(Http.deleteRelative(s"/api/terminals")) >>
          Effect.action(UpdateTerminals(value.terminals.diff(List(value.selectedTerminal.get))))
      )


    case ConnectTerminal =>
      if (value.terminalConnected) {
        effectOnly(Effect.action(Nothing))
      } else {
        val id = value.selectedTerminal.get.id
        val container = document.getElementById("terminal-container")

        val xTerminal = new XTerminal()
        val fitAddon = new FitAddon
        xTerminal.loadAddon(fitAddon)

        xTerminal.options.allowProposedApi = true
        xTerminal.options.cursorBlink = true

        xTerminal.onData(data => if (!value.loadingTerminalHistory) EventBus.sendMessage(s"terminal-$id-stdin", data))
        xTerminal.open(container)
        fitAddon.fit()

        window.addEventListener("resize", (_: Event) =>
          if (value.xTerminal.isDefined) {
            val now = Instant.now()
            if (lastResize.isEmpty || (now.toEpochMilli - lastResize.get.toEpochMilli) > 250) {
              fitAddon.fit()
              EventBus.sendMessage(s"terminal-$id-resize", Map("rows" -> value.xTerminal.get.rows, "columns" -> value.xTerminal.get.cols).asJson.noSpaces)
              lastResize = Some(Instant.now())
            }
          }
        )

        EventBus.subscribe(s"terminal-$id-stdout", message => if (value.xTerminal.isDefined) value.xTerminal.get.write(message))
        EventBus.subscribe(s"terminal-$id-stderr", message => if (value.xTerminal.isDefined) value.xTerminal.get.write(message))

        effectOnly(
          Effect.action(UpdateXTerminal(Some(xTerminal))) >>
          Effect.action(UpdateTerminalConnected(true)) >>
          Effect(Http.getRelative(s"/api/terminals/$id/connect/${fitAddon.proposeDimensions().rows}/${fitAddon.proposeDimensions().cols}")) >>
          Effect.action(UpdateLoadingTerminalHistory(true)) >>
          Effect(Http.getRelativeAs[List[TerminalHistory]](s"/api/terminals/$id/history").map(history =>
            history.getOrElse(List()).foreach(line => value.xTerminal.get.writeln(line.message))
          )) >>
          Effect.action(UpdateLoadingTerminalHistory(false))
        )
      }


    case DisconnectTerminal =>
      if (!value.terminalConnected)
        effectOnly(Effect.action(Nothing))
      else
        effectOnly(
          Effect(Http.getRelative(s"/api/terminals/${value.selectedTerminal.get.id}/disconnect")) >>
          Effect.action(UpdateTerminalConnected(false))
        )


    case RestartTerminal =>
      effectOnly(
        Effect(Http.getRelative(s"/api/terminals/${value.selectedTerminal.get.id}/restart"))
      )


    case SelectTerminal(terminal) =>
      effectOnly(
        Effect.action(UpdateSelectedTerminal(Some(terminal)))
      )


    case ClearTerminal =>
      effectOnly(
        Effect(Http.getRelative(s"/api/terminals/${value.selectedTerminal.get.id}/clear")) >>
        Effect(Future(value.xTerminal.get.clear()))
      )


    case RefreshTerminal =>
      effectOnly(
        Effect.action(Nothing)
      )


    case ScrollTerminalToTop =>
      effectOnly(
        Effect(Future(value.xTerminal.get.scrollToTop()))
      )


    case ScrollTerminalToBottom =>
      effectOnly(
        Effect(Future(value.xTerminal.get.scrollToBottom()))
      )


    case CopyFromTerminal =>
      effectOnly(
        Effect {
          if (value.xTerminal.isDefined) {
            val selection = value.xTerminal.get.getSelection()
            navigator.clipboard.writeText(selection).toFuture
          } else Future()
        }
      )


    case PasteToTerminal =>
      effectOnly(
        Effect {
          navigator.clipboard.readText.toFuture.map { text =>
            println("FOUND TEXT: " + text)
            EventBus.sendMessage(s"terminal-${value.selectedTerminal.get.id}-stdin", text)
          }
        }
      )


    case UpdateLoadingTerminalHistory(loading) =>
      updated(value.copy(loadingTerminalHistory = loading))


    case UpdateSelectedTerminal(terminal) =>
      updated(value.copy(selectedTerminal = terminal))


    case UpdateTerminals(terminals) =>
      updated(value.copy(terminals = terminals))


    case UpdateTerminalConnected(terminalConnected) =>
      updated(value.copy(terminalConnected = terminalConnected))


    case UpdateXTerminal(terminal) =>
      updated(value.copy(xTerminal = terminal))

  }
}