package com.harana.sdk.shared.models.flow

import com.harana.sdk.shared.models.common.Entity.EntityId
import com.harana.sdk.shared.models.common.User.UserId
import com.harana.sdk.shared.models.common.{Entity, Status}
import com.harana.sdk.shared.models.designer.flow.container.EnvironmentVariable
import com.harana.sdk.shared.models.flow.Flow.FlowId
import com.harana.sdk.shared.models.flow.FlowExecution.FlowExecutionId
import com.harana.sdk.shared.models.flow.execution.spark._
import com.harana.sdk.shared.utils.Random
import io.circe.generic.JsonCodec

import java.time.Instant

@JsonCodec
case class FlowExecution(flowId: FlowId,
                         actionExecutions: List[ActionExecution],
                         outputVariables: Map[ActionTypeInfo.Id, List[EnvironmentVariable]],
                         sparkMetrics: Option[SparkMetrics],
                         startTime: Option[Instant],
                         acceptedTime: Option[Instant],
                         endTime: Option[Instant],
                         createdBy: Option[UserId],
                         created: Instant,
                         updatedBy: Option[UserId],
                         updated: Instant,
                         id: FlowExecutionId,
                         status: Status,
                         executionStatus: ExecutionStatus,
                         executionFailure: Option[String],
                         version: Long,
                         tags: Set[String],
                         relationships: Map[String, EntityId])
  extends Entity with Serializable {
  type EntityType = FlowExecution
}

object FlowExecution {
  type FlowExecutionId = String

  def apply(flowId: FlowId, createdBy: Option[UserId]): FlowExecution =
    apply(flowId, List(), Map(), None, None, None, None, createdBy, Instant.now, createdBy, Instant.now, Random.long, Status.Active, ExecutionStatus.None, None, 1L, Set(), Map())

}