package com.harana.workflowexecutor.partialexecution

import com.harana.sdk.backend.models.flow.graph.FlowGraph.FlowNode
import com.harana.sdk.backend.models.flow.graph.GraphKnowledge._
import com.harana.sdk.backend.models.flow.graph._
import com.harana.sdk.backend.models.flow.inference.InferContext
import com.harana.sdk.shared.models.designer.flow.graph.node.NodeStatus.{Completed, Draft, Queued, Running}
import com.harana.sdk.backend.models.flow.utils.Logging
import com.harana.sdk.backend.models.flow.workflows.NodeStateWithResults
import com.harana.sdk.backend.models.flow.{Action, graph, workflows}
import com.harana.sdk.shared.models.designer.flow.graph.node.Node
import com.harana.sdk.shared.models.designer.flow.{ActionObjectInfo, EntitiesMap, ExecutionReport, NodeState}
import com.harana.sdk.shared.models.designer.flow.exceptions.{CyclicGraphError, FailureCode, FailureDescription, HaranaError}
import com.harana.sdk.shared.models.designer.flow.graph.{Edge, Endpoint, FlowGraph, TopologicallySortable}
import com.harana.sdk.shared.models.designer.flow.report.ReportContent
import com.harana.sdk.shared.models.designer.flow.utils.Id
import com.harana.shared.models.HaranaFile

import scala.util.{Failure, Success, Try}

case class StatefulGraph(directedGraph: FlowGraph,
                         states: Map[Node.Id, NodeStateWithResults],
                         executionFailure: Option[FailureDescription]) extends TopologicallySortable[Action] with KnowledgeInference with NodeInference with Logging {

  require(states.size == directedGraph.nodes.size, "A graph should know states of all its nodes (and only its nodes)!")

  // Tells the graph that an execution of a node has started.
  def nodeStarted(id: Node.Id): StatefulGraph = changeState(id)(_.start)

  // Tells the graph that an exception has occurred during the execution of a node.
  def nodeFailed(id: Node.Id, cause: Exception): StatefulGraph = {
    val description = cause match {
      case e: HaranaError => e.failureDescription
      case e => genericNodeFailureDescription(e)
    }
    changeState(id)(_.fail(description))
  }

  // Tells the graph that the execution of a node has successfully finished. */
  def nodeFinished(id: Node.Id, entitiesIds: Seq[Id], reports: Map[Id, ReportContent], actionObjects: Map[Id, ActionObjectInfo]) =
    changeState(id)(_.finish(entitiesIds, reports, actionObjects))

  // Tells the graph that the execution has failed. Eg. there was an issue other than node exception that makes execution impossible.
  def fail(errors: FailureDescription) = {
    val updatedStates = abortUnfinished(states)
    copy(states = updatedStates, executionFailure = Some(errors))
  }

  // Lists all nodes that can be executed (that is: all their predecessors completed successfully).
  def readyNodes: Seq[ReadyNode] = {
    val queuedIds = states.collect { case (id, nodeState) if nodeState.isQueued => id }
    val inputs = queuedIds.collect { case id if predecessorsReady(id) => (id, inputFor(id).get) }
    inputs.map { case (id, input) => ReadyNode(directedGraph.node(id), input) }.toSeq
  }

  def runningNodes =
    states
      .collect { case (id, nodeState) if nodeState.isRunning => id }
      .map(id => node(id))
      .toSeq

  // Tells the graph that it was enqueued for execution.
  def enqueue = {
    if (isRunning) throw new IllegalStateException("Cannot enqueue running graph")
    val updatedStates = states.mapValues(state => state.enqueue)
    copy(states = updatedStates)
  }

  def isRunning = states.valuesIterator.exists(state => state.isQueued || state.isRunning)
  def withoutFailedNodes = !hasFailedNodes
  def hasFailedNodes = states.valuesIterator.exists(_.isFailed)
  def size = directedGraph.size
  def node(id: Node.Id) = directedGraph.node(id)
  def nodes = directedGraph.nodes

  override def topologicallySorted = directedGraph.topologicallySorted
  override def allPredecessorsOf(id: Id) = directedGraph.allPredecessorsOf(id)
  override def predecessors(id: Id) = directedGraph.predecessors(id)
  override def edges = directedGraph.edges
  override def successors(id: Id) = directedGraph.successors(id)

  // Tells the graph to infer Knowledge. Graph checks if it is still correct in context of the inferred knowledge.
  def inferAndApplyKnowledge(context: InferContext): StatefulGraph = {
    Try(inferKnowledge(context, memorizedKnowledge)) match {
      case Success(knowledge) => handleInferredKnowledge(knowledge)
      case Failure(ex: CyclicGraphError) => fail(StatefulGraph.cyclicGraphFailureDescription)
      case Failure(ex) => fail(StatefulGraph.genericFailureDescription(ex))
    }
  }

  def memorizedKnowledge =
    graph.GraphKnowledge(states.flatMap { case (nodeId, nodeState) =>
      nodeState.knowledge.map(knowledge => (nodeId, knowledge))
    })

  def updateStates(changedGraph: StatefulGraph) =
    copy(states = states ++ changedGraph.states, executionFailure = changedGraph.executionFailure)

  def subgraph(nodes: Set[Node.Id]) = {
    val directedSubgraph = directedGraph.subgraph(nodes)
    val subStatuses = states.filter { case (id, _) => directedSubgraph.nodes.map(_.id).contains(id) }
    copy(directedSubgraph, subStatuses)
  }

  def draft(nodeId: Node.Id) =
    copy(states = markChildrenDraft(states, nodeId))

  def clearKnowledge(nodeId: Node.Id) =
    copy(states = clearChildrenKnowledge(states, nodeId))

  def enqueueDraft =
    copy(states = states.mapValues(state => if (state.isDraft) state.enqueue else state))

  def abortQueued =
    copy(states = states.mapValues(state => if (state.isQueued) state.abort else state))

  def executionReport: ExecutionReport =
    ExecutionReport(states.mapValues(_.nodeState), executionFailure)

  def notExecutedNodes: Set[Node.Id] =
    states.collect { case (nodeId, state) if state.isDraft || state.isAborted => nodeId }.toSet

  private def markChildrenDraft(states: Map[Node.Id, NodeStateWithResults], draftNodeId: Node.Id): Map[Node.Id, NodeStateWithResults] =
    recursiveStateUpdate(states, draftNodeId, _.draft)

  private def clearChildrenKnowledge(states: Map[Node.Id, NodeStateWithResults], nodeToClearId: Node.Id): Map[Node.Id, NodeStateWithResults] =
    recursiveStateUpdate(states, nodeToClearId, _.clearKnowledge)

  private def recursiveStateUpdate(states: Map[Node.Id, NodeStateWithResults], nodeId: Node.Id, updateNodeState: (NodeStateWithResults => NodeStateWithResults)): Map[Node.Id, NodeStateWithResults] = {
    val children = directedGraph.successorsOf(nodeId)
    val previousState = states.get(nodeId)
    val updatedState = previousState.map(s => states.updated(nodeId, updateNodeState(s))).getOrElse(states)
    if (children.isEmpty)
      updatedState
    else {
      children.toSeq.foldLeft(updatedState)((states, node) => recursiveStateUpdate(states, node, updateNodeState))
    }
  }

  protected def handleInferredKnowledge(knowledge: GraphKnowledge) = {
    if (knowledge.errors.nonEmpty) {
      val description = FailureDescription(
        Id.randomId,
        FailureCode.IncorrectWorkflow,
        "Incorrect workflow",
        Some("Provided workflow cannot be launched, because it contains errors")
      )
      copy(states = updateStates(knowledge), executionFailure = Some(description))
    } else {
      val updatedStates = states.map { case (nodeId, nodeState) =>
        (nodeId, nodeState.withKnowledge(knowledge.getResult(nodeId)))
      }
      copy(states = updatedStates)
    }
  }

  protected def updateStates(knowledge: GraphKnowledge) = {
    knowledge.errors.toSeq.foldLeft(states) { case (modifiedStates, (id, nodeErrors)) =>
      // Inner workflow nodes are not in the states map; don't try to update their state
      if (states.contains(id))
        modifiedStates.updated(id, states(id).fail(nodeErrorsFailureDescription(id, nodeErrors)))
      else
        modifiedStates
    }
  }

  protected def nodeErrorsFailureDescription(nodeId: Node.Id, nodeErrors: InferenceErrors) = {
    FailureDescription(
      Id.randomId,
      FailureCode.IncorrectNode,
      title = "Incorrect node",
      message = Some(
        "Node contains errors that prevent workflow from being executed:\n\n" +
          nodeErrors.map(e => "* " + e.message).mkString("\n")
      )
    )
  }

  protected def predecessorsReady(id: Node.Id) =
    StatefulGraph.predecessorsReady(id, directedGraph, states)

  protected def inputFor(id: Node.Id): Option[Seq[ActionObjectInfo]] = {
    if (predecessorsReady(id)) {
      val entities = directedGraph.predecessors(id).flatten.map { case Endpoint(predecessorId, portIndex) =>
        states(predecessorId) match {
          case NodeStateWithResults(NodeState(Completed(_, _, results), _), actionObjects, _) => actionObjects(results(portIndex))
          case NodeStateWithResults(NodeState(otherStatus, _), _, _) =>
            throw new IllegalStateException(
              s"Cannot collect inputs for node ${directedGraph.node(id)} because one of its predecessors was in '$otherStatus' " +
                s"instead of Completed: ${directedGraph.node(predecessorId)}"
            )
        }
      }
      Some(entities)
    } else
      None
  }

  private def changeState(id: Node.Id)(f: (NodeStateWithResults) => NodeStateWithResults): StatefulGraph = {
    val updatedStates = {
      val newNodeState = f(states(id))
      val withNodeUpdated = states.updated(id, newNodeState)
      val successorsOfFailedAborted = if (newNodeState.isFailed) abortSuccessors(withNodeUpdated, id) else withNodeUpdated
      if (nodeRunningOrReadyNodeExist(successorsOfFailedAborted)) successorsOfFailedAborted else abortUnfinished(successorsOfFailedAborted)
    }
    copy(states = updatedStates)
  }

  protected def genericNodeFailureDescription(exception: Exception): FailureDescription = {
    FailureDescription(
      Id.randomId,
      FailureCode.UnexpectedError,
      "Execution of a node failed",
      Some(s"Error while executing a node: ${exception.getMessage}"),
      FailureDescription.stacktraceDetails(exception.getStackTrace)
    )
  }

  private def nodeRunningOrReadyNodeExist(states: Map[Node.Id, NodeStateWithResults]) = {
    val readyNodeExists = states.exists {
      case (id, s) if s.isQueued => StatefulGraph.predecessorsReady(id, directedGraph, states)
      case _ => false
    }
    states.values.exists(_.isRunning) || readyNodeExists
  }

  private def abortUnfinished(unfinished: Map[Id, NodeStateWithResults]) =
    unfinished.mapValues(abortIfAbortable)

  private def abortIfAbortable(nodeStateWithResults: NodeStateWithResults) =
    nodeStateWithResults match {
      case NodeStateWithResults(state @ NodeState(status, _), _, _) =>
        val newStatus = status match {
          case _: Running | _: Queued | _: Draft => status.abort
          case _ => status
        }
      nodeStateWithResults.copy(nodeState = state.copy(nodeStatus = newStatus))
  }

  private def abortSuccessors(allNodes: Map[Id, NodeStateWithResults], id: Node.Id) = {
    val unsuccessfulAtBeginning: Set[FlowNode] = Set(node(id))
    val sorted = {
      val sortedOpt = topologicallySorted
      assert(sortedOpt.nonEmpty)
      sortedOpt.get
    }
    // if A is a predecessor of B, we will visit A first, so if A is failed/aborted, B will also be aborted.
    val (_, updatedStates) = sorted.foldLeft[(Set[FlowNode], Map[Id, NodeStateWithResults])](unsuccessfulAtBeginning, allNodes) {
      case ((unsuccessfulNodes, statesMap), node) =>
        if (allPredecessorsOf(node.id).intersect(unsuccessfulNodes).nonEmpty)
          (unsuccessfulNodes + node, statesMap.updated(node.id, abortIfAbortable(statesMap(node.id))))
        else
          (unsuccessfulNodes, statesMap)
    }
    updatedStates
  }

}

object StatefulGraph {

  def apply(nodes: Set[FlowNode] = Set(), edges: Set[Edge] = Set()): StatefulGraph = {
    val states = nodes.map(node => node.id -> workflows.NodeStateWithResults(NodeState(Draft(), Some(EntitiesMap())), Map(), None)).toMap
    StatefulGraph(FlowGraph(nodes, edges), states, None)
  }

  protected def predecessorsReady(id: Node.Id, directedGraph: FlowGraph, states: Map[Node.Id, NodeStateWithResults]) = {
    directedGraph.predecessors(id).forall {
      case Some(Endpoint(nodeId, _)) => states(nodeId).isCompleted
      case None => false
    }
  }

  def cyclicGraphFailureDescription =
    FailureDescription(
      Id.randomId,
      FailureCode.IncorrectWorkflow,
      "Cyclic workflow",
      Some("Provided workflow cannot be launched, because it contains a cycle")
    )

  def genericFailureDescription(e: Throwable) =
    FailureDescription(
      Id.randomId,
      FailureCode.LaunchingFailure,
      "Launching failure",
      Some(s"Error while launching workflow: ${e.getMessage}"),
      FailureDescription.stacktraceDetails(e.getStackTrace)
    )
}

case class ReadyNode(node: FlowNode, input: Seq[ActionObjectInfo])
