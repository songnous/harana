package com.harana.sdk.shared.models.flow

import com.harana.sdk.shared.models.common.Entity.EntityId
import com.harana.sdk.shared.models.common.Id
import com.harana.sdk.shared.models.flow.Flow.FlowId
import com.harana.sdk.shared.models.flow.FlowExecution.FlowExecutionId
import com.harana.sdk.shared.models.flow.execution.spark.ExecutionLog
import com.harana.sdk.shared.utils.Random
import io.circe.generic.JsonCodec

@JsonCodec
case class FlowExecutionLogs(flowId: FlowId,
                             flowExecutionId: FlowExecutionId,
                             logs: List[ExecutionLog],
                             id: EntityId = Random.long) extends Id
