package com.harana.executor.spark.metrics

import com.harana.sdk.backend.models.designer.flow.Action.ActionId
import com.harana.sdk.backend.models.designer.flow.FlowExecution.FlowExecutionId
import com.harana.sdk.backend.models.designer.flow.execution.ExecutionStatus
import com.harana.sdk.backend.models.designer.flow.{ActionExecution, Flow, FlowExecution, FlowExecutionLogs}
import com.harana.sdk.shared.models.designer.flow.ActionInfo

import java.time.Instant
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object MetricsManager {
  private val dirtyFlowExecutions = mutable.HashSet[FlowExecutionId]()
  private val flowExecutions = mutable.Map[FlowExecutionId, (FlowExecution, FlowExecutionLogs)]()
  private val activeActions = mutable.Map[FlowExecutionId, ListBuffer[ActionId]]()


  def isDirty(flowExecutionId: FlowExecutionId): Boolean =
    dirtyFlowExecutions.contains(flowExecutionId)


  def clearDirty(flowExecutionId: FlowExecutionId) =
    dirtyFlowExecutions.remove(flowExecutionId)


  def startFlow(flow: Flow, flowExecution: FlowExecution): Unit =
    updateFlow(flowExecution.copy(acceptedTime = Some(Instant.now), executionStatus = ExecutionStatus.Executing), new FlowExecutionLogs(flow.id, flowExecution.id, List()))


  def stopFlow(flowExecutionId: FlowExecutionId): Unit = {
    activeActions(flowExecutionId).clear()
    updateFlow(flowExecutions(flowExecutionId)._1.copy(completedTime = Some(Instant.now), executionStatus = ExecutionStatus.Succeeded), flowExecutions(flowExecutionId)._2)
  }


  def clearFlow(flowExecutionId: FlowExecutionId): Unit =
    flowExecutions -= flowExecutionId


  def updateFlow(flowExecution: FlowExecution, flowLogs: FlowExecutionLogs): Unit = {
    flowExecutions += flowExecution.id -> (flowExecution, flowLogs)
    activeActions += flowExecution.id -> new ListBuffer[ActionId]()
    dirtyFlowExecutions += flowExecution.id
  }


  def hasFlowExecution(id: FlowExecutionId): Boolean =
    flowExecutions.contains(id)


  def getFlowExecutionAndLogs(flowExecutionId: FlowExecutionId): (FlowExecution, FlowExecutionLogs) = {
    flowExecutions(flowExecutionId)
  }


  def startAction(flowExecutionId: FlowExecutionId, actionId: ActionId): Unit = {
    activeActions(flowExecutionId) += actionId
    updateAction(flowExecutionId, ActionExecution(actionId, 50, ExecutionStatus.Executing, None))
  }


  def stopAction(flowExecutionId: FlowExecutionId, actionId: ActionId): Unit = {
    activeActions(flowExecutionId) -= actionId
    updateAction(flowExecutionId, ActionExecution(actionId, 100, ExecutionStatus.Succeeded, None))
  }


  def failAction(flowExecutionId: FlowExecutionId, actionId: ActionId, message: String): Unit = {
    activeActions(flowExecutionId).clear()
    updateAction(flowExecutionId, ActionExecution(actionId, 50, ExecutionStatus.Failed, Some(message)))
    updateFlow(flowExecutions(flowExecutionId)._1.copy(completedTime = Some(Instant.now), executionStatus = ExecutionStatus.Failed, executionFailure = Some(message)), flowExecutions(flowExecutionId)._2)
  }


  private def updateAction(flowExecutionId: FlowExecutionId, newActionExecution: ActionExecution): Unit = {
    val flowExecution = flowExecutions(flowExecutionId)._1
    val newActionExecutions = flowExecution.actionExecutions.find(_.actionId == newActionExecution.actionId) match {
      case Some(old) => flowExecution.actionExecutions.updated(flowExecution.actionExecutions.indexOf(old), newActionExecution)
      case None => flowExecution.actionExecutions :+ newActionExecution
    }
    dirtyFlowExecutions += flowExecutionId
    updateFlow(flowExecution.copy(actionExecutions = newActionExecutions), flowExecutions(flowExecution.id)._2)
  }
}