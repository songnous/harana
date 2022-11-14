package com.harana.designer.frontend.schedules.list

import com.harana.sdk.shared.models.schedules.{Action, Event, Schedule}
import diode.{Action => DiodeAction}

import scala.collection.mutable.ListBuffer

object ScheduleListStore {

  case class State(actionTypes: List[String],
                   eventTypes: List[String],
                   item: Option[Schedule],
                   itemActions: ListBuffer[Action],
                   itemEvents: ListBuffer[Event])

  case class AddAction(action: Action) extends DiodeAction
  case class DeleteAction(index: Int) extends DiodeAction
  case class MoveAction(fromIndex: Int, toIndex: Int) extends DiodeAction
  case class UpdateAction(index: Int, newAction: Action) extends DiodeAction

  case class AddEvent(event: Event) extends DiodeAction
  case class DeleteEvent(index: Int) extends DiodeAction
  case class MoveEvent(fromIndex: Int, toIndex: Int) extends DiodeAction
  case class UpdateEvent(index: Int, newEvent: Event) extends DiodeAction

}