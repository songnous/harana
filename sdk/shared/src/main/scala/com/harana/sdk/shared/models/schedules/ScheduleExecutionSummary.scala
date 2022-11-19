package com.harana.sdk.shared.models.schedules

import com.harana.sdk.shared.models.schedules.Schedule.ScheduleId
import io.circe.generic.JsonCodec

import java.time.{Duration, Instant}

@JsonCodec
case class ScheduleExecutionSummary(scheduleId: ScheduleId,
                                    started: Instant,
                                    duration: Option[Long],
                                    executionStatus: ScheduleExecutionStatus)

object ScheduleExecutionSummary {
  def apply(execution: ScheduleExecution): ScheduleExecutionSummary =
    ScheduleExecutionSummary(
      execution.id,
      execution.started,
      execution.finished.map(f => Duration.between(f, execution.started).toMillis),
      execution.executionStatus
    )
}