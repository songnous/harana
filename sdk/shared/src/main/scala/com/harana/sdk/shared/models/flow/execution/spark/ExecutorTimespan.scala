package com.harana.sdk.shared.models.flow.execution.spark

import io.circe.generic.JsonCodec

import java.time.Instant

@JsonCodec
case class ExecutorTimespan(executorId: String,
                            hostId: String,
                            startTime: Instant,
                            endTime: Instant,
                            cores: Int,
                            metrics: AggregateMetrics)