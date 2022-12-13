package com.harana.designer.frontend.schedules.item

import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.schedules.item.ScheduleItemStore._
import com.harana.designer.frontend.utils.http.Http
import com.harana.sdk.shared.models.schedules.Schedule
import diode._

import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

class ScheduleItemHandler extends ActionHandler(zoomTo(_.scheduleItemState)) {

  def handle = {

    case Init(preferences) =>
      noChange


    case OpenSchedule(id) =>
      effectOnly(
        Effect(Http.getRelativeAs[Schedule](s"/api/schedules/$id").map(schedule => UpdateSchedule(schedule)))
      )


    case UpdateSchedule(schedule) =>
      updated(value.copy(schedule = schedule))

  }
}