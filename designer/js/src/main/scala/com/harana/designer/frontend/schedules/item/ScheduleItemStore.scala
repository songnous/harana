package com.harana.designer.frontend.schedules.item

import com.harana.sdk.shared.models.schedules.Schedule.ScheduleId
import com.harana.sdk.shared.models.schedules.Schedule
import diode.{Action => DiodeAction}

object ScheduleItemStore {

  case class State(schedule: Option[Schedule],
                   actionTypes: List[String],
                   eventTypes: List[String])

  val initialState = State(None, List(), List())


  case class Init(userPreferences: Map[String, String]) extends DiodeAction

  case class OpenSchedule(scheduleId: ScheduleId) extends DiodeAction
  case class UpdateSchedule(schedule: Option[Schedule]) extends DiodeAction

  case class UpdateActionTypes(actionTypes: List[String]) extends DiodeAction
  case class UpdateEventTypes(eventTypes: List[String]) extends DiodeAction
}