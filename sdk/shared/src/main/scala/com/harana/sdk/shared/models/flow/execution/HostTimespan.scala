package com.harana.sdk.shared.models.designer.flow.execution

import java.time.Instant

import io.circe.generic.JsonCodec
import com.harana.sdk.shared.utils.CirceCodecs._

@JsonCodec
case class HostTimespan(hostId: String,
                        startTime: Instant,
                        endTime: Instant,
                        metrics: AggregateMetrics)