package com.harana.sdk.shared.models.flow.graph

import Edge.PortIndex
import com.harana.sdk.shared.models.flow.graph.node.Node
import com.harana.sdk.shared.models.flow.utils.Id

import scala.annotation.tailrec

abstract class DirectedGraph[T <: GraphAction, G <: DirectedGraph[T, G]](
    val nodes: Set[Node[T]] = Set[Node[T]](),
    val edges: Set[Edge] = Set()
) extends TopologicallySortable[T] with Serializable {

  val validEdges = filterValidEdges(nodes, edges)

  private val idToNode = nodes.map(n => n.id -> n).toMap
  private val _predecessors = preparePredecessors
  private val _successors = prepareSuccessors
  private val _containsCycle = new TopologicalSort(this).isSorted

  def topologicallySorted: Option[List[Node[T]]] = new TopologicalSort(this).sortedNodes

  def node(id: Id): Node[T] = idToNode(id)

  def predecessors(id: Id) = _predecessors(id)
  def successors(id: Id) = _successors(id)
  def containsCycle = _containsCycle

  def allPredecessorsOf(id: Id) =
    predecessors(id).foldLeft(Set[Node[T]]())((acc: Set[Node[T]], predecessor: Option[Endpoint]) =>
      predecessor match {
        case None => acc
        case Some(endpoint) => (acc + node(endpoint.nodeId)) ++ allPredecessorsOf(endpoint.nodeId)
      }
    )

  def size: Int = nodes.size

  def rootNodes: Seq[Node[T]] =
    topologicallySorted.get.filter(n => predecessors(n.id).flatten.isEmpty)

  def predecessorsOf(nodes: Set[Id]): Set[Id] =
    nodes.flatMap(node => predecessors(node).flatten.map(_.nodeId))

  def successorsOf(node: Id): Set[Id] =
    successors(node).flatMap(endpoints => endpoints.map(_.nodeId)).toSet

  def subgraph(nodes: Set[Id]): G = {
    @tailrec
    def collectNodesEdges(previouslyCollectedNodes: Set[Id], previouslyCollectedEdges: Set[Edge], toProcess: Set[Id]): (Set[Id], Set[Edge]) = {
      // Do not revisit nodes (in case of a cycle).
      val nodesPredecessors = predecessorsOf(toProcess) -- previouslyCollectedNodes
      val nextNodes = previouslyCollectedNodes ++ nodesPredecessors
      val nextEdges = previouslyCollectedEdges ++ edgesOf(toProcess)

      if (toProcess.isEmpty)
        (nextNodes, nextEdges)
      else
        collectNodesEdges(nextNodes, nextEdges, nodesPredecessors)
    }

    val (n, e) = collectNodesEdges(nodes, Set(), nodes)
    subgraph(n.map(node), e)
  }

  def subgraph(nodes: Set[Node[T]], edges: Set[Edge]): G

  def getValidEdges: Set[Edge] = validEdges
  private def edgesOf(nodes: Set[Id]): Set[Edge] = nodes.flatMap(edgesTo)
  private def edgesTo(node: Id): Set[Edge] = validEdges.filter(edge => edge.to.nodeId == node)

  private def preparePredecessors = {
    import scala.collection.mutable
    val mutablePredecessors = mutable.Map.empty[Id, mutable.IndexedSeq[Option[Endpoint]]]

    nodes.foreach(n => mutablePredecessors += n.id -> mutable.IndexedSeq.fill(n.value.inArity)(None))

    validEdges.foreach(edge => mutablePredecessors(edge.to.nodeId)(edge.to.portIndex) = Some(edge.from))
    mutablePredecessors.view.mapValues(_.toIndexedSeq).toMap
  }

  private def prepareSuccessors = {
    import scala.collection.mutable
    val mutableSuccessors = mutable.Map.empty[Id, IndexedSeq[mutable.Set[Endpoint]]]

    nodes.foreach(node => mutableSuccessors += node.id -> Vector.fill(node.value.outArity)(mutable.Set()))
    validEdges.foreach(edge => mutableSuccessors(edge.from.nodeId)(edge.from.portIndex) += edge.to)
    mutableSuccessors.view.mapValues(_.map(_.toSet)).toMap
  }

  private def filterValidEdges(nodes: Set[Node[T]], edges: Set[Edge]) = {
    val nodesIds = nodes.map(_.id)
    edges.filter(edge =>
      {
        val inNodeOpt  = nodes.find(n => n.id == edge.from.nodeId)
        val outNodeOpt = nodes.find(n => n.id == edge.to.nodeId)
        for {
          inNode  <- inNodeOpt
          outNode <- outNodeOpt
        } yield edge.from.portIndex < inNode.value.outArity && edge.to.portIndex < outNode.value.inArity
      }.getOrElse(false)
    )
  }
}
