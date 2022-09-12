package com.harana.sdk.shared.models.schedules

import com.harana.sdk.shared.models.common.Event.EventId
import io.circe.generic.JsonCodec

import java.time.Instant

@JsonCodec
case class ScheduleExecution(executed: Instant,
                             executionStatus: ScheduleExecutionStatus,
                             completedEvents: List[EventId],
//                             completedActions: List[ActionId],
//                             failedActions: List[ActionId],
                             errorMessages: List[String])