package com.harana.sdk.shared.models.flow

import com.harana.sdk.shared.models.designer.flow.execution.ExecutionStatus
import io.circe.generic.JsonCodec

@JsonCodec
case class ActionExecution(actionId: ActionId,
                           percentage: Int,
                           executionStatus: ExecutionStatus,
                           executionFailure: Option[String])
