package com.harana.sdk.shared.models.flow

import com.harana.sdk.shared.models.common.Entity.EntityId
import com.harana.sdk.shared.models.common.User.UserId
import com.harana.sdk.shared.models.common.{Entity, Status}
import com.harana.sdk.shared.models.designer.flow.execution._
import com.harana.sdk.shared.models.flow.Flow.FlowId
import com.harana.sdk.shared.models.flow.FlowExecution.FlowExecutionId
import com.harana.sdk.shared.utils.Random
import io.circe.generic.JsonCodec

import java.time.Instant

@JsonCodec
case class FlowExecution(flowId: FlowId,
                         actionExecutions: List[ActionExecution],
                         info: Option[ApplicationInfo],
                         metrics: Option[AggregateMetrics],
                         executorCount: Option[Int],
                         executorMemory: Option[Int],
                         maximumExecutorCount: Option[Int],
                         maximumExecutorMemory: Option[Int],
                         coresPerExecutor: Option[Int],
                         hostMap: Map[String, HostTimespan],
                         executorMap: Map[String, ExecutorTimespan],
                         jobMap: Map[Long, JobTimespan],
                         stageMap: Map[Int, StageTimespan],
                         acceptedTime: Option[Instant],
                         completedTime: Option[Instant],
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

  def apply(flowId: FlowId,
            createdBy: Option[UserId],
            executionStatus: ExecutionStatus,
            maximumExecutorCount: Option[Int],
            maximumExecutorMemory: Option[Int]): FlowExecution =
    apply(flowId, List(), None, None, None, None, maximumExecutorCount, maximumExecutorMemory, None, Map(), Map(), Map(), Map(), None, None, createdBy, Instant.now, createdBy, Instant.now, Random.long, Status.Active, executionStatus, None, 1L, Set(), Map())

  def apply(flowId: FlowId,
            actionExecutions: List[ActionExecution],
            info: ApplicationInfo,
            metrics: AggregateMetrics,
            coresPerExecutor: Int,
            hostMap: Map[String, HostTimespan],
            executorMap: Map[String, ExecutorTimespan],
            jobMap: Map[Long, JobTimespan],
            stageMap: Map[Int, StageTimespan],
            createdBy: Option[UserId],
            executionStatus: ExecutionStatus,
            executorCount: Option[Int],
            executorMemory: Option[Int],
            maximumExecutorCount: Option[Int],
            maximumExecutorMemory: Option[Int],
            acceptedTime: Option[Instant],
            completedTime: Option[Instant]): FlowExecution =
    apply(flowId, actionExecutions, Some(info), Some(metrics), executorCount, executorMemory, maximumExecutorCount, maximumExecutorCount, Some(coresPerExecutor), hostMap, executorMap, jobMap, stageMap, acceptedTime, completedTime, createdBy, Instant.now, createdBy, Instant.now, Random.long, Status.Active, executionStatus, None, 1L, Set(), Map())
}