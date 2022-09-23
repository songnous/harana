package com.harana.sdk.shared.models.flow.execution.spark

import io.circe.generic.JsonCodec

import java.time.Instant

@JsonCodec
case class StageTimespan(stageId: Int,
                         startTime: Instant,
                         endTime: Instant,
                         numberOfTasks: Long,
                         metrics: AggregateMetrics,
                         minTaskLaunchTime: Instant,
                         maxTaskFinishTime: Instant,
                         parentStageIds: List[Int],
                         taskExecutionTimes: List[Int],
                         taskPeakMemoryUsage: List[Long])