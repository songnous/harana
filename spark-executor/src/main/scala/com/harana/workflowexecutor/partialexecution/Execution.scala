package com.harana.workflowexecutor.partialexecution

import com.harana.sdk.backend.models.designer.flow.graph._
import com.harana.sdk.backend.models.designer.flow.inference.InferContext
import com.harana.sdk.backend.models.designer.flow.workflows.NodeStateWithResults
import com.harana.sdk.shared.models.designer.flow.graph.node.Node
import com.harana.sdk.shared.models.designer.flow.{ActionObjectInfo, ExecutionReport}
import com.harana.sdk.shared.models.designer.flow.graph.FlowGraph
import com.harana.sdk.shared.models.designer.flow.report.ReportContent
import com.harana.sdk.shared.models.designer.flow.utils.Id

object Execution {

  def empty = IdleExecution(StatefulGraph(), Set.empty[Node.Id])

  def apply(graph: StatefulGraph, selectedNodes: Set[Node.Id] = Set.empty) = IdleExecution(graph, selectedNodes)

  def selectedNodes(directedGraph: FlowGraph, nodes: Seq[Id]) = {
    val graphNodeIds = directedGraph.nodes.map(_.id)
    nodes.filter(graphNodeIds.contains).toSet
  }

  def defaultExecutionFactory(graph: StatefulGraph) = Execution(graph)

}

sealed abstract class Execution {

  final def node(id: Node.Id) = graph.node(id)

  def executionReport = ExecutionReport(graph.states.mapValues(_.nodeState), graph.executionFailure)

  type NodeStates = Map[Node.Id, NodeStateWithResults]

  def graph: StatefulGraph
  def nodeStarted(id: Node.Id): Execution
  def nodeFailed(id: Node.Id, cause: Exception): Execution
  def nodeFinished(id: Node.Id, resultsIds: Seq[Id], reports: Map[Id, ReportContent], actionObjects: Map[Id, ActionObjectInfo]): Execution
  def enqueue: Execution
  def inferAndApplyKnowledge(inferContext: InferContext): Execution
  def abort: Execution
}

case class IdleExecution(override val graph: StatefulGraph, selectedNodes: Set[Node.Id] = Set.empty) extends Execution {

  require(graph.readyNodes.isEmpty, "Idle executor must not have ready nodes")

  override def nodeFinished(id: Node.Id, resultsIds: Seq[Id], reports: Map[Id, ReportContent], actionObjects: Map[Id, ActionObjectInfo]) =
    throw new IllegalStateException("A node cannot finish in IdleExecution")

  override def nodeFailed(id: Id, cause: Exception) =
    throw new IllegalStateException("A node cannot fail in IdleExecution")

  override def nodeStarted(id: Id) =
    throw new IllegalStateException("A node cannot start in IdleExecution")

  def updateStructure(newStructure: FlowGraph, nodes: Set[Id] = Set.empty) = {
    val selected = Execution.selectedNodes(newStructure, nodes.toSeq)
    val substructure = newStructure.subgraph(selected)
    val newStates = findStates(newStructure, substructure, selected)
    val graph = StatefulGraph(newStructure, newStates, None)
    IdleExecution(graph, selected)
  }

  override def enqueue = {
    val (selected: Set[Id], subgraph: StatefulGraph) = selectedSubgraph
    val enqueuedSubgraph = subgraph.enqueueDraft
    if (enqueuedSubgraph.isRunning)
      RunningExecution(graph, enqueuedSubgraph, selected)
    else
      this
  }

  override def inferAndApplyKnowledge(inferContext: InferContext) = {
    val (_, subgraph: StatefulGraph) = selectedSubgraph
    val inferred = subgraph.inferAndApplyKnowledge(inferContext)
    copy(graph = graph.updateStates(inferred))
  }

  override def abort = throw new IllegalStateException("IdleExecution cannot be aborted!")

  private def selectedSubgraph: (Set[Id], StatefulGraph) = {
    val selected = Execution.selectedNodes(graph.directedGraph, selectedNodes.toSeq)
    val subgraph = graph.subgraph(selected)
    (selected, subgraph)
  }

  private def actionParametersChanged(newNode: FlowGraph.FlowNode, graph: StatefulGraph): Boolean = {
    graph.nodes.find(_.id == newNode.id) match {
      case Some(oldNode) => !newNode.value.sameAs(oldNode.value)
      case None => true
    }
  }

  private def findStates(newStructure: FlowGraph, substructure: FlowGraph, nodes: Set[Node.Id]): NodeStates = {
    val noMissingStates = newStructure.nodes.map { case Node(id, _) =>
      id -> graph.states.getOrElse(id, NodeStateWithResults.draft)
    }.toMap

    if (newStructure.containsCycle)
      noMissingStates.mapValues(_.draft.clearKnowledge)
    else {
      val wholeGraph = StatefulGraph(newStructure, noMissingStates, None)
      val newNodes = newStructure.nodes.map(_.id).diff(graph.directedGraph.nodes.map(_.id))

      val nodesToExecute = substructure.nodes.filter { case Node(id, _) =>
        nodes.contains(id) || !wholeGraph.states(id).isCompleted
      }.map(_.id)

      val predecessorsChangedNodes = newStructure.nodes.map(_.id).diff(newNodes).filter { id =>
        newStructure.predecessors(id) != graph.predecessors(id)
      }

      val changedParameters = newStructure.nodes.collect {
        case node if actionParametersChanged(node, graph) => node.id
      }

      val changedNodes = predecessorsChangedNodes ++ changedParameters
      val nodesNeedingDrafting = newNodes ++ nodesToExecute ++ changedNodes
      val transformGraph = (draftNodes(nodesNeedingDrafting) _).andThen(clearNodesKnowledge(changedNodes))
      transformGraph(wholeGraph).states
    }
  }

  private def draftNodes(nodesNeedingDrafting: Set[Node.Id])(graph: StatefulGraph) = {
    nodesNeedingDrafting.foldLeft(graph) { case (g, id) => g.draft(id) }
  }

  private def clearNodesKnowledge(nodesToClear: Set[Node.Id])(graph: StatefulGraph) = {
    nodesToClear.foldLeft(graph) { case (g, id) => g.clearKnowledge(id) }
  }

}

abstract class StartedExecution(fullGraph: StatefulGraph, runningPart: StatefulGraph, selectedNodes: Set[Node.Id]) extends Execution {

  override def graph = {
    val mergedStates = fullGraph.states ++ runningPart.states
    // Assumes runningPart is subgraph of fullGraph
    StatefulGraph(fullGraph.directedGraph, mergedStates, runningPart.executionFailure)
  }

  override def nodeFinished(id: Node.Id, resultsIds: Seq[Id], reports: Map[Id, ReportContent], actionObjects: Map[Id, ActionObjectInfo]) =
    withRunningPartUpdated(_.nodeFinished(id, resultsIds, reports, actionObjects))

  override def nodeFailed(id: Id, cause: Exception) =
    withRunningPartUpdated(_.nodeFailed(id, cause))

  override def enqueue =
    throw new IllegalStateException("An Execution that is not idle cannot be enqueued!")

  override def inferAndApplyKnowledge(inferContext: InferContext) =
    throw new IllegalStateException("An Execution that is not idle cannot infer knowledge!")

  final def withRunningPartUpdated(update: (StatefulGraph) => StatefulGraph) = {
    val updatedRunningPart = update(runningPart)
    val updatedFullGraph = fullGraph.updateStates(updatedRunningPart)

    if (updatedRunningPart.isRunning)
      copyGraphs(updatedRunningPart, updatedFullGraph)
    else
      IdleExecution(updatedFullGraph, selectedNodes)
  }

  protected def copyGraphs(updatedRunningPart: StatefulGraph, updatedFullGraph: StatefulGraph): Execution

}

case class RunningExecution(fullGraph: StatefulGraph, runningPart: StatefulGraph, selectedNodes: Set[Node.Id])
    extends StartedExecution(fullGraph, runningPart, selectedNodes) {

  override def nodeStarted(id: Id) = {
    val updatedRunningPart = runningPart.nodeStarted(id)
    val updatedFullGraph = fullGraph.updateStates(updatedRunningPart)
    copy(fullGraph = updatedFullGraph, runningPart = updatedRunningPart)
  }

  override def abort =
    AbortedExecution(graph, runningPart.abortQueued, selectedNodes)

  override protected def copyGraphs(updatedRunningPart: StatefulGraph, updatedGraph: StatefulGraph) =
    RunningExecution(updatedGraph, updatedRunningPart, selectedNodes)
}

case class AbortedExecution(fullGraph: StatefulGraph, runningPart: StatefulGraph, selectedNodes: Set[Node.Id])
    extends StartedExecution(fullGraph, runningPart, selectedNodes) {

  override def nodeStarted(id: Id) = throw new IllegalStateException("A node cannot be started when execution is Aborted!")
  override def abort = throw new IllegalStateException("Once aborted execution cannot be aborted again!")

  override protected def copyGraphs(updatedRunningPart: StatefulGraph, updatedFullGraph: StatefulGraph) =
    AbortedExecution(updatedFullGraph, updatedRunningPart, selectedNodes)

}
