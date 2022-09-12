package com.harana.sdk.shared.models.designer.flow.execution

import java.time.Instant
import com.harana.sdk.shared.utils.CirceCodecs._

import io.circe.generic.JsonCodec

@JsonCodec
case class ExecutionLog(level: String,
                        message: String,
                        timestamp: Instant)