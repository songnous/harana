package com.harana.sdk.shared.models.designer.flow.execution

import java.time.Instant

import io.circe.generic.JsonCodec

import scala.collection.mutable
import com.harana.sdk.shared.utils.CirceCodecs._

@JsonCodec
case class JobTimespan(jobId: String,
                       startTime: Instant,
                       endTime: Instant,
                       metrics: AggregateMetrics,
                       stageMap: Map[Int, StageTimespan])