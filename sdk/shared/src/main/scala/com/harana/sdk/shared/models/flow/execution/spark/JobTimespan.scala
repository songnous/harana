package com.harana.sdk.shared.models.flow.execution.spark

import io.circe.generic.JsonCodec

import java.time.Instant

@JsonCodec
case class JobTimespan(jobId: String,
                       startTime: Instant,
                       endTime: Instant,
                       metrics: AggregateMetrics,
                       stageMap: Map[Int, StageTimespan])