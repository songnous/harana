package com.harana.sdk.backend.models.flow.graph

import com.harana.sdk.backend.models.flow._
import com.harana.sdk.backend.models.flow.actiontypes.{ActionTypeType0To1, ActionTypeType1To1, ActionTypeType2To2}
import com.harana.sdk.shared.models.flow.graph.FlowGraph.FlowNode
import com.harana.sdk.shared.models.designer.flow.graph.Endpoint
import com.harana.sdk.shared.models.flow.actionobjects.ActionObjectInfo
import com.harana.sdk.shared.models.flow.exceptions.FailureDescription
import com.harana.sdk.shared.models.flow.graph.{Edge, GraphAction}
import com.harana.sdk.shared.models.flow.graph.node.{Node, NodeStatus}
import com.harana.sdk.shared.models.flow.utils.Id
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar

import java.time.Instant
import java.time.temporal.ChronoUnit

trait GraphTestSupport { self: MockitoSugar =>

  val op0To1 = {
    val m = mock[ActionTypeType0To1[ActionObjectInfo]]
    when(m.sameAs(any())).thenReturn(true)
    m
  }

  val op1To1 = createOp1To1

  def createOp1To1: ActionTypeType1To1[ActionObjectInfo, ActionObjectInfo] = {
    val m = mock[ActionTypeType1To1[ActionObjectInfo, ActionObjectInfo]]
    when(m.sameAs(any())).thenReturn(true)
    m
  }

  val op2To2 = {
    val m = mock[ActionTypeType2To2[ActionObjectInfo, ActionObjectInfo, ActionObjectInfo, ActionObjectInfo]]
    when(m.sameAs(any())).thenReturn(true)
    m
  }

  /** Creates edges for a graph like this one: A -(1)-> B -(2)-> C -(3)-> D \ \ \ (5) \ \ \ ->
    * ---(4)---> E To each node assigns the specified Id.
    */

  val nodesSeq = generateNodes(op0To1, op1To1, op1To1, op1To1, op2To2)
  val nodeSet = nodesSeq.map(_._2).toSet

  val idA :: idB :: idC :: idD :: idE :: Nil = nodesSeq.map(_._1).toList

  val nodeA :: nodeB :: nodeC :: nodeD :: nodeE :: Nil = nodesSeq.map(_._2).toList

  val edgeList: List[Edge] = edges(idA, idB, idC, idD, idE)
  val edge1 :: edge2 :: edge3 :: edge4 :: edge5 :: Nil = edgeList
  val edgeSet = edgeList.toSet

  val nodeIds = Seq(idA, idB, idC, idD, idE)

  val results = Map(
    idA -> Seq(mock[Id]),
    idB -> Seq(mock[Id]),
    idC -> Seq(mock[Id]),
    idD -> Seq(mock[Id]),
    idE -> Seq(mock[Id], mock[Id])
  )

  private def edges(idA: Id, idB: Id, idC: Id, idD: Id, idE: Id): List[Edge] = {
    List(
      Edge((idA, 0), (idB, 0)),
      Edge((idB, 0), (idC, 0)),
      Edge((idC, 0), (idD, 0)),
      Edge((idA, 0), (idE, 0)),
      Edge((idB, 0), (idE, 1))
    )
  }

  def generateNodes(ops: GraphAction*): Seq[(Id, FlowNode)] = {
    val nodes = ops.map(o => Node(Node.Id.randomId, o))
    nodes.map(n => n.id -> n)
  }

  def nodeRunning = NodeStatus.Running(Instant.now)

  def nodeFailed = NodeStatus.Running(Instant.now).fail(mock[FailureDescription])

  def nodeCompleted = {
    val date = Instant.now
    NodeStatus.Completed(date, date.plus(1, ChronoUnit.MINUTES), Seq())
  }

  def nodeCompletedId(nodeId: Id) = {
    val date = Instant.now
    NodeStatus.Completed(date, date.plus(1, ChronoUnit.MINUTES), results(nodeId))
  }
}
