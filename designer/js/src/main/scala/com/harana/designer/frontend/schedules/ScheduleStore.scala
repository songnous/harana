package com.harana.designer.frontend.schedules

import com.harana.sdk.shared.models.schedules.Action.ActionId
import com.harana.sdk.shared.models.schedules.Event.EventId
import com.harana.sdk.shared.models.schedules.{Action, Event, Schedule, ScheduleExecution}
import diode.{Action => DiodeAction}

import scala.collection.mutable.ListBuffer

object ScheduleStore {

  case class State(itemActions: ListBuffer[(ActionId, Action)],
                   itemEvents: ListBuffer[(EventId, Event)],
                   history: List[(ScheduleExecution, Schedule)])

  val initialState = State(ListBuffer.empty, ListBuffer.empty, List())

  case class AddAction(action: Action) extends DiodeAction
  case class DeleteAction(index: Int) extends DiodeAction
  case class MoveAction(fromIndex: Int, toIndex: Int) extends DiodeAction
  case class UpdateAction(index: Int, newAction: Action) extends DiodeAction

  case class AddEvent(event: Event) extends DiodeAction
  case class DeleteEvent(index: Int) extends DiodeAction
  case class MoveEvent(fromIndex: Int, toIndex: Int) extends DiodeAction
  case class UpdateEvent(index: Int, newEvent: Event) extends DiodeAction

  case class UpdateItemActions(actions: List[(ActionId, Action)]) extends DiodeAction
  case class UpdateItemEvents(events: List[(EventId, Event)]) extends DiodeAction
  case class UpdateHistory(history: List[(ScheduleExecution, Schedule)]) extends DiodeAction

}