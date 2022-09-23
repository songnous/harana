package com.harana.workflowexecutor

import akka.actor._
import com.harana.sdk.backend.models.flow._
import com.harana.sdk.backend.models.flow.graph.FlowGraph.FlowNode
import com.harana.sdk.backend.models.flow.graph._
import com.harana.sdk.backend.models.flow.utils.Logging
import com.harana.sdk.backend.models.flow.workflows.InferredState
import com.harana.sdk.shared.models.designer.flow.flows.{Workflow, WorkflowWithResults}
import com.harana.sdk.shared.models.designer.flow.graph.node.Node
import com.harana.sdk.shared.models.designer.flow.{ActionObjectInfo, ExecutionReport}
import com.harana.sdk.shared.models.designer.flow.exceptions.{FailureCode, FailureDescription}
import com.harana.sdk.shared.models.designer.flow.json.graph.NodeStatusJsonProtocol
import com.harana.sdk.shared.models.designer.flow.report.ReportContent
import com.harana.sdk.shared.models.designer.flow.utils.Id
import com.harana.shared.models.HaranaFile
import com.harana.workflowexecutor.WorkflowExecutorActor.Messages._
import com.harana.workflowexecutor.WorkflowManagerClientActorProtocol.{SaveState, SaveWorkflow}
import com.harana.workflowexecutor.partialexecution._

/** WorkflowExecutorActor coordinates execution of a workflow by distributing work to WorkflowNodeExecutorActors and collecting results. */
abstract class WorkflowExecutorActor(
    val executionContext: CommonExecutionContext,
    nodeExecutorFactory: GraphNodeExecutorFactory,
    workflowManagerClientActor: Option[ActorRef],
    publisher: Option[ActorRef],
    terminationListener: Option[ActorRef],
    executionFactory: StatefulGraph => Execution
) extends Actor with Logging with NodeStatusJsonProtocol {

  val progressReporter = WorkflowProgress()
  val workflowId = Workflow.Id.fromString(self.path.name)

  private[workflowexecutor] var statefulWorkflow: StatefulWorkflow = null

  def ready(): Receive = {
    case Launch(nodes) => launch(nodes)
    case UpdateStruct(workflow) => updateStruct(workflow)
    case Synchronize() => synchronize()
  }

  def launched(): Receive = {
    waitingForFinish().orElse {
      case Abort() => abort()
      case NodeStarted(id) => nodeStarted(id)
    }
  }

  def waitingForFinish(): PartialFunction[Any, Unit] = {
    case NodeCompleted(id, nodeExecutionResult) => nodeCompleted(id, nodeExecutionResult)
    case NodeFailed(id, failureDescription) => nodeFailed(id, failureDescription)
    case Synchronize() => synchronize()
    case UpdateStruct(workflow) => updateStruct(workflow)
    case l: Launch => logger.info("It is illegal to Launch a graph when the execution is in progress.")
  }

  def initWithWorkflow(workflowWithResults: WorkflowWithResults): Unit = {
    statefulWorkflow = StatefulWorkflow(executionContext, workflowWithResults, executionFactory)
    context.become(ready())
    sendInferredState(statefulWorkflow.inferState)
    onInitiated()
  }

  def synchronize(): Unit = {
    sendExecutionReport(statefulWorkflow.executionReport)
    sendInferredState(statefulWorkflow.inferState)
  }

  private def updateStruct(workflow: Workflow): Unit = {
    val removedNodes = statefulWorkflow.getNodesRemovedByWorkflow(workflow)
    statefulWorkflow.updateStructure(workflow)

    removedNodes.foreach { node =>
      val nodeRef = getGraphNodeExecutor(node, List.empty)
      nodeRef ! WorkflowNodeExecutorActor.Messages.Delete()
    }

    val workflowWithResults = statefulWorkflow.workflowWithResults
    workflowManagerClientActor.foreach(_ ! SaveWorkflow(workflowWithResults))
    sendInferredState(statefulWorkflow.inferState)
  }

  def sendInferredState(inferredState: InferredState): Unit = {
    if (inferredState == null) logger.error("Inferred state is null. Not sending message")
    else publisher.foreach(_ ! inferredState)
  }

  private def abort(): Unit = {
    val startingPointExecution = statefulWorkflow.currentExecution
    statefulWorkflow.abort()
    updateExecutionState(startingPointExecution)
    statefulWorkflow.currentExecution.graph.runningNodes.foreach(node =>
      executionContext.sparkContext.cancelJobGroup(node.id.toString)
    )
  }

  def launch(nodes: Set[Node.Id]): Unit = {
    val startingPointExecution = statefulWorkflow.currentExecution
    val nodesToExecute = if (nodes.isEmpty) startingPointExecution.graph.notExecutedNodes else nodes
    statefulWorkflow.launch(nodesToExecute)
    updateExecutionState(startingPointExecution)
  }

  private def updateExecutionState(startingPointExecution: Execution): Unit = {
    val inferredState = statefulWorkflow.currentExecution match {

      case _: IdleExecution =>
        terminationListener.foreach(_ ! statefulWorkflow.executionReport)
        context.unbecome()
        context.become(ready())
        Some(statefulWorkflow.inferState)

      case _: RunningExecution =>
        launchReadyNodes()
        context.unbecome()
        context.become(launched())
        None

      case _: AbortedExecution =>
        context.unbecome()
        context.become(waitingForFinish())
        None

      case _: StartedExecution =>
        throw new NotImplementedError("Case for StartedExecution not implemented in WorkflowExecutorActor")
    }

    val executionReport = statefulWorkflow.changesExecutionReport(startingPointExecution)
    sendExecutionReport(executionReport)

    if (inferredState.isDefined) sendInferredState(inferredState.get)
  }

  def sendExecutionReport(executionReport: ExecutionReport): Unit = {
    logger.debug(s"Status for '$workflowId': Error: ${executionReport.error}, States of nodes: ${executionReport.nodesStatuses.mkString("\n")}")
    publisher.foreach(_ ! executionReport)
    workflowManagerClientActor.foreach(_ ! SaveState(workflowId, executionReport))
  }

  def launchReadyNodes() =
    statefulWorkflow.startReadyNodes().foreach { readyNode =>
      val input = readyNode.input.toList
      val nodeRef = getGraphNodeExecutor(readyNode.node, input)
      nodeRef ! WorkflowNodeExecutorActor.Messages.Start()
      logger.debug(s"Starting node $readyNode")
    }

  private def getGraphNodeExecutor(node: FlowNode, doactionObject: List[ActionObjectInfo]): ActorRef = {
    val nodeExecutionContext = executionContext.createExecutionContext(workflowId, node.id)
    nodeExecutorFactory.createGraphNodeExecutor(context, nodeExecutionContext, node, doactionObject)
  }

  def nodeStarted(id: Node.Id): Unit = logger.debug("{}", NodeStarted(id))

  def nodeCompleted(id: Node.Id, results: NodeExecutionResults): Unit = {
    logger.debug(s"Node ${statefulWorkflow.node(id)} completed!")
    val startingPointExecution = statefulWorkflow.currentExecution
    statefulWorkflow.nodeFinished(id, results.entitiesId, results.reports, results.actionObjects)
    finalizeNodeExecutionEnd(startingPointExecution)
  }

  def nodeFailed(id: Node.Id, cause: Exception): Unit = {
    logger.warn(s"Node ${statefulWorkflow.node(id)} failed!", cause)
    val startingPointExecution = statefulWorkflow.currentExecution
    statefulWorkflow.nodeFailed(id, cause)
    finalizeNodeExecutionEnd(startingPointExecution)
  }

  def finalizeNodeExecutionEnd(startingPointExecution: Execution): Unit = {
    progressReporter.logProgress(statefulWorkflow.currentExecution)
    updateExecutionState(startingPointExecution)
  }

  def execution: Execution = statefulWorkflow.currentExecution
  protected def onInitiated(): Unit = {}
}

object WorkflowExecutorActor {
  object Messages {
    sealed trait Message
    case class Launch(nodes: Set[Node.Id] = Set.empty) extends Message
    case class NodeStarted(nodeId: Node.Id) extends Message
    case class NodeCompleted(id: Node.Id, results: NodeExecutionResults) extends Message
    case class NodeFailed(id: Node.Id, cause: Exception) extends Message
    case class Abort() extends Message
    case class Init() extends Message
    case class UpdateStruct(workflow: Workflow) extends Message
    case class Synchronize() extends Message
  }

  def inferenceErrorsDebugDescription(graphKnowledge: GraphKnowledge) =
    FailureDescription(
      Id.randomId,
      FailureCode.IncorrectWorkflow,
      title = "Incorrect workflow",
      message = Some("Provided workflow cannot be launched, because it contains errors"),
      details = graphKnowledge.errors.map { case (id, errors) => (id.toString, errors.map(_.toString).mkString("\n")) }
    )
}

trait GraphNodeExecutorFactory {
  def createGraphNodeExecutor(context: ActorContext, executionContext: ExecutionContext, node: FlowNode, input: List[ActionObjectInfo]): ActorRef
}

class GraphNodeExecutorFactoryImpl extends GraphNodeExecutorFactory {
  override def createGraphNodeExecutor(context: ActorContext, executionContext: ExecutionContext, node: FlowNode, input: List[ActionObjectInfo]) = {
    val props = Props(new WorkflowNodeExecutorActor(executionContext, node, input)).withDispatcher("node-executor-dispatcher")
    context.actorOf(props, s"node-executor-${node.id.value.toString}")
  }
}

// This is a separate class in order to make logs look better.
case class WorkflowProgress() extends Logging {

  def logProgress(execution: Execution): Unit = {
    val states = execution.graph.states.values
    val completed = states.count(_.isCompleted)
    logger.info(
      s"$completed ${if (completed == 1) "node" else "nodes"} successfully completed, " +
        s"${states.count(_.isFailed)} failed, " +
        s"${states.count(_.isAborted)} aborted, " +
        s"${states.count(_.isRunning)} running, " +
        s"${states.count(_.isQueued)} queued."
    )
  }
}

case class NodeExecutionResults(entitiesId: Seq[Id], reports: Map[Id, ReportContent], actionObjects: Map[Id, ActionObjectInfo])
