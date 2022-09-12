package com.harana.sdk.shared.models.designer.flow.execution

import java.time.Instant

import io.circe.generic.JsonCodec

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import com.harana.sdk.shared.utils.CirceCodecs._

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