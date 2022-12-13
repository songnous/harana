package com.harana.designer.frontend.schedules.list

import com.harana.sdk.shared.models.schedules.{Action, Event, Schedule, ScheduleExecution}
import diode.{Action => DiodeAction}

import scala.collection.mutable.ListBuffer

object ScheduleListStore {

  case class State(item: Option[Schedule],
                   itemActions: ListBuffer[Action],
                   itemEvents: ListBuffer[Event],
                   scheduleHistory: List[(ScheduleExecution, Schedule)])

  val initialState = State(None, ListBuffer.empty, ListBuffer.empty, List())

  case class AddAction(action: Action) extends DiodeAction
  case class DeleteAction(index: Int) extends DiodeAction
  case class MoveAction(fromIndex: Int, toIndex: Int) extends DiodeAction
  case class UpdateAction(index: Int, newAction: Action) extends DiodeAction

  case class AddEvent(event: Event) extends DiodeAction
  case class DeleteEvent(index: Int) extends DiodeAction
  case class MoveEvent(fromIndex: Int, toIndex: Int) extends DiodeAction
  case class UpdateEvent(index: Int, newEvent: Event) extends DiodeAction

  case class UpdateItem(item: Option[Schedule]) extends DiodeAction
  case class UpdateItemActions(actions: ListBuffer[Action]) extends DiodeAction
  case class UpdateItemEvents(events: ListBuffer[Event]) extends DiodeAction
  case class UpdateScheduleHistory(scheduleHistory: List[(ScheduleExecution, Schedule)]) extends DiodeAction

}