package com.harana.workflowexecutor.partialexecution

import com.harana.sdk.backend.models.flow.{CommonExecutionContext, workflows}
import com.harana.sdk.backend.models.flow.utils.Logging
import com.harana.sdk.backend.models.flow.workflows.{InferredState, NodeStateWithResults}
import com.harana.sdk.shared.models.designer.flow.flows._
import com.harana.sdk.shared.models.designer.flow.graph.node.Node
import com.harana.sdk.shared.models.designer.flow.ActionObjectInfo
import com.harana.sdk.shared.models.designer.flow.report.ReportContent
import com.harana.sdk.shared.models.designer.flow.utils.Id
import io.circe.Json

class StatefulWorkflow(
    private val executionContext: CommonExecutionContext,
    val workflowId: Id,
    val workflowInfo: WorkflowInfo,
    private val thirdPartyData: Json,
    private val startingExecution: Execution,
    private val stateInferrer: StateInferrer
) extends Logging {

  private var execution = startingExecution
  private var additionalData = thirdPartyData

  def getNodesRemovedByWorkflow(workflow: Workflow) = {
    val previousNodes  = execution.graph.nodes
    val newNodes = workflow.graph.nodes
    val removedNodesId = previousNodes.map(node => node.id).diff(newNodes.map(node => node.id))
    previousNodes.filter(node => removedNodesId.contains(node.id))
  }

  def launch(nodes: Set[Node.Id]) =
    execution match {
      case idleExecution: IdleExecution =>
        val newExecution = idleExecution.updateStructure(execution.graph.directedGraph, nodes)
        val inferred = newExecution.inferAndApplyKnowledge(executionContext.inferContext)
        val map = inferred.graph.executionFailure.map(_ => inferred)
        execution = map.getOrElse(inferred.enqueue)
      case notIdle => throw new IllegalStateException(s"Only IdleExecution can be launched. Execution: $notIdle")
    }

  def startReadyNodes() = {
    val readyNodes = execution.graph.readyNodes
    execution = readyNodes.foldLeft(execution) { case (runningExecution, readyNode) =>
      runningExecution.nodeStarted(readyNode.node.id)
    }
    readyNodes
  }

  def currentExecution = execution
  def currentAdditionalData = additionalData
  def executionReport = execution.executionReport

  def changesExecutionReport(startingPointExecution: Execution) = ExecutionReport(getChangedNodes(startingPointExecution), execution.graph.executionFailure)

  def workflowWithResults = WorkflowWithResults(
    workflowId,
    metadata,
    execution.graph.directedGraph,
    additionalData,
    executionReport,
    workflowInfo
  )

  def node(id: Node.Id) = execution.node(id)
  def nodeStarted(id: Node.Id) = execution = execution.nodeStarted(id)
  def nodeFinished(id: Node.Id, entitiesIds: Seq[Id], reports: Map[Id, ReportContent], actionObjects: Map[Id, ActionObjectInfo]) =
    execution = execution.nodeFinished(id, entitiesIds, reports, actionObjects)

  def nodeFailed(id: Node.Id, cause: Exception) = execution = execution.nodeFailed(id, cause)

  def abort() =
    execution = execution.abort

  // When execution is running struct update will be ignored.
  def updateStructure(workflow: Workflow) = {
    execution = execution match {
      case idleExecution: IdleExecution => idleExecution.updateStructure(workflow.graph)
      case _ =>
        logger.warn("Update of the graph during execution is impossible. Only `thirdPartyData` updated.")
        execution
    }
    additionalData = workflow.additionalData
  }

  def inferState = stateInferrer.inferState(execution)

  private def getChangedNodes(startingPointExecution: Execution) = {
    execution.graph.states.filterNot { case (id, stateWithResults) =>
      startingPointExecution.graph.states.contains(id) &&
      stateWithResults.clearKnowledge == startingPointExecution.graph.states(id).clearKnowledge
    }.mapValues(_.nodeState)
  }
}

object StatefulWorkflow extends Logging {

  def apply(executionContext: CommonExecutionContext, workflow: WorkflowWithResults, executionFactory: StatefulGraph => Execution) = {
    val states = workflow.executionReport.states
    val noMissingStates = workflow.graph.nodes.map { node =>
      states.get(node.id).map(state => node.id -> NodeStateWithResults(state.draft, Map(), None)).getOrElse(node.id -> NodeStateWithResults.draft)
    }.toMap

    val graph = StatefulGraph(workflow.graph, noMissingStates, workflow.executionReport.error)
    val execution = executionFactory(graph)

    new StatefulWorkflow(
      executionContext,
      workflow.id,
      workflow.metadata,
      workflow.workflowInfo,
      workflow.thirdPartyData,
      execution,
      new DefaultStateInferrer(executionContext, workflow.id)
    )
  }
}

trait StateInferrer {
  def inferState(execution: Execution): InferredState
}

class DefaultStateInferrer(executionContext: CommonExecutionContext, workflowId: Id) extends StateInferrer {
  override def inferState(execution: Execution) = {
    val knowledge = execution.graph.inferKnowledge(executionContext.inferContext, execution.graph.memorizedKnowledge)
    workflows.InferredState(workflowId, knowledge, execution.executionReport.statesOnly)
  }
}
