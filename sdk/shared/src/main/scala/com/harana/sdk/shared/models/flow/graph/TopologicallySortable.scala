package com.harana.sdk.shared.models.flow.graph

import Edge.PortIndex
import com.harana.sdk.shared.models.flow.graph.node.Node
import com.harana.sdk.shared.models.flow.utils.Id

trait TopologicallySortable[T] {

  def topologicallySorted: Option[List[Node[T]]]

  def allPredecessorsOf(id: Id): Set[Node[T]]

  def edges: Set[Edge]

  def nodes: Set[Node[T]]

  def node(id: Id): Node[T]

  def predecessors(id: Id): IndexedSeq[Option[Endpoint]]

  def successors(id: Id): IndexedSeq[Set[Endpoint]]

}
