package com.harana.designer.frontend.terminal

import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.system.SystemStore.{EventBusConnected, EventBusDisconnected}
import com.harana.designer.frontend.terminal.TerminalStore._
import com.harana.designer.frontend.utils.http.Http
import com.harana.designer.frontend.{Circuit, EventBus, Main, State}
import com.harana.sdk.shared.models.terminals.{HistoryType, Terminal, TerminalHistory}
import diode.AnyAction._
import diode._
import io.circe.parser.decode
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._
import typings.std.global.navigator

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future

class TerminalHandler extends ActionHandler(zoomTo(_.terminalState)) {

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
      if (value.terminalConnected)
        effectOnly(Effect.action(Nothing))
      else {
        val id = value.selectedTerminal.get.id

        EventBus.subscribe(s"terminal-$id-stdout", message =>
          value.selectedTerminalRef.current.terminal.write(message)
        )

        EventBus.subscribe(s"terminal-$id-stderr", message =>
          value.selectedTerminalRef.current.terminal.write(message)
        )

        effectOnly(
          Effect(Http.getRelative(s"/api/terminals/$id/connect")) >>
            Effect.action(UpdateTerminalConnected(true))
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
        Effect(Future(value.selectedTerminalRef.current.terminal.clear()))
      )


    case RefreshTerminal =>
      effectOnly(
        Effect.action(Nothing)
      )


    case ScrollTerminalToTop =>
      effectOnly(
        Effect(Future(value.selectedTerminalRef.current.terminal.scrollToTop()))
      )


    case ScrollTerminalToBottom =>
      effectOnly(
        Effect(Future(value.selectedTerminalRef.current.terminal.scrollToBottom()))
      )


    case CopyFromTerminal =>
      effectOnly(
        Effect {
          val selection = value.selectedTerminalRef.current.terminal.getSelection()
          println(s"Selection = $selection")
          navigator.clipboard.writeText(selection).toFuture
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


    case UpdateSelectedTerminal(terminal) =>
      updated(value.copy(selectedTerminal = terminal))


    case UpdateTerminals(terminals) =>
      updated(value.copy(terminals = terminals))


    case UpdateTerminalConnected(terminalConnected) =>
      updated(value.copy(terminalConnected = terminalConnected))


    case UpdateTerminalHistory(terminalHistory) =>
      updated(value.copy(terminalHistory = terminalHistory))

  }
}