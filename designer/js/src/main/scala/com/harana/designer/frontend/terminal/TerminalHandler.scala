package com.harana.designer.frontend.terminal

import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.system.SystemStore.EventBusConnected
import com.harana.designer.frontend.terminal.TerminalStore._
import com.harana.designer.frontend.utils.http.Http
import com.harana.designer.frontend.{Circuit, EventBus, Main, State}
import com.harana.sdk.shared.models.terminals.{HistoryType, Terminal, TerminalHistory}
import diode.AnyAction._
import diode._
import io.circe.parser.decode
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future

class TerminalHandler extends ActionHandler(zoomTo(_.terminalState)) {

  override def handle: PartialFunction[Any, ActionResult[State]] = {

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
      val id = value.selectedTerminal.get.id

      effectOnly(
        Effect(Http.getRelative(s"/api/terminals/$id/connect"))
      )


    case DisconnectTerminal =>
      effectOnly(
        Effect(Http.getRelative(s"/api/terminals/${value.selectedTerminal.get.id}/disconnect"))
      )


    case RestartTerminal =>
      effectOnly(
        Effect(Http.getRelative(s"/api/terminals/${value.selectedTerminal.get.id}/restart"))
      )


    case SelectTerminal(terminal) =>
      effectOnly(
        Effect.action(UpdateSelectedTerminal(Some(terminal)))
      )


    case UpdateSelectedTerminal(terminal) =>
      updated(value.copy(
        selectedTerminal = terminal,
        selectedTerminalHistory = if (terminal.isDefined) ListBuffer.from(terminal.get.history) else ListBuffer.empty
      ))


    case UpdateTerminals(terminals) =>
      updated(value.copy(terminals = terminals))


    case AddToTerminalHistory(message) =>
      value.selectedTerminalHistory += message
      updated(value)


    case EventBusConnected =>
      if (value.selectedTerminal.isDefined) {
        val id = value.selectedTerminal.get.id
        EventBus.subscribe(s"terminal-$id-stdout", message => {
          Circuit.dispatch(AddToTerminalHistory(TerminalHistory(HistoryType.Stdout, message)))
        })
      }
      effectOnly(Effect.action(Nothing))
  }
}