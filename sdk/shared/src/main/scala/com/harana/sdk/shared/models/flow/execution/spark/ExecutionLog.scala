package com.harana.sdk.shared.models.flow.execution.spark

import io.circe.generic.JsonCodec

import java.time.Instant

@JsonCodec
case class ExecutionLog(level: String,
                        message: String,
                        timestamp: Instant)