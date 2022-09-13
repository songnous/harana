package com.harana.workflowexecutor.executor

import com.harana.sdk.backend.models.flow._
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actions.exceptions.CustomActionExecutionError
import com.harana.sdk.shared.models.designer.flow.flows._
import com.harana.sdk.shared.BuildInfo
import com.harana.sdk.shared.models.designer.flow.graph.node.Node
import com.harana.sdk.shared.models.designer.flow.{ActionObjectInfo, ExecutionReport, FlowType}
import com.harana.sdk.shared.models.designer.flow.parameters.custom.InnerWorkflow
import com.harana.sdk.shared.models.designer.flow.utils.Entity
import com.harana.workflowexecutor.NodeExecutionResults
import com.harana.workflowexecutor.partialexecution._


class InnerWorkflowExecutorImpl extends InnerWorkflowExecutor with InnerWorkflowJsonProtocol {

  override def parse(workflow: Json) = workflow.as[InnerWorkflow]
  override def toJson(innerWorkflow: InnerWorkflow): Json = innerWorkflow.asJson.asJson

  override def execute(executionContext: CommonExecutionContext, innerWorkflow: InnerWorkflow, dataFrame: DataFrame) = {

    val workflowId = Workflow.Id.randomId

    val workflowWithResults = WorkflowWithResults(
      workflowId,
      FlowType.Batch,
      BuildInfo.version,
      innerWorkflow.graph,
      innerWorkflow.thirdPartyData,
      ExecutionReport(Map()),
      WorkflowInfo.forId(workflowId)
    )

    val statefulWorkflow = StatefulWorkflow(executionContext, workflowWithResults, Execution.defaultExecutionFactory)

    val nodesToExecute = statefulWorkflow.currentExecution.graph.nodes.map(_.id)
    statefulWorkflow.launch(nodesToExecute)

    statefulWorkflow.currentExecution.executionReport.error.map { e =>
      throw CustomActionExecutionError(e.title + "\n" + e.message.getOrElse("") + "\n" + e.details.values.mkString("\n"))
    }

    statefulWorkflow.nodeStarted(innerWorkflow.source.id)

    nodeCompleted(statefulWorkflow, innerWorkflow.source.id, nodeExecutionResultsFrom(Vector(dataFrame)))

    run(statefulWorkflow, executionContext)

    statefulWorkflow.currentExecution.graph.states(innerWorkflow.sink.id).actionObjects.head._2.asInstanceOf[DataFrame]
  }

  private def run(statefulWorkflow: StatefulWorkflow, executionContext: CommonExecutionContext): Unit = {
    statefulWorkflow.currentExecution match {
      case _: RunningExecution =>
        statefulWorkflow.startReadyNodes().foreach { readyNode =>
          val input = readyNode.input.toVector
          val nodeExecutionContext = executionContext.createExecutionContext(statefulWorkflow.workflowId, readyNode.node.id)
          val results = executeAction(readyNode.node, input, nodeExecutionContext)
          val nodeResults = nodeExecutionResultsFrom(results)
          nodeCompleted(statefulWorkflow, readyNode.node.id, nodeResults)
        }
        run(statefulWorkflow, executionContext)
      case _ => ()
    }
  }

  private def executeAction(node: FlowNode, input: Vector[ActionObjectInfo], executionContext: ExecutionContext) = {
    val inputKnowledge = input.map(actionObject => Knowledge(actionObject))
    node.value.inferKnowledgeUntyped(inputKnowledge)(executionContext.inferContext)
    node.value.executeUntyped(input)(executionContext)
  }

  private def nodeExecutionResultsFrom(actionResults: Vector[ActionObjectInfo]) = {
    val results = actionResults.map(actionObject => (Entity.Id.randomId, actionObject))
    NodeExecutionResults(results.map(_._1), Map(), results.toMap)
  }

  private def nodeCompleted(statefulWorkflow: StatefulWorkflow, id: Node.Id, results: NodeExecutionResults) = {
    statefulWorkflow.nodeFinished(id, results.entitiesId, results.reports, results.actionObjects)
  }
}
