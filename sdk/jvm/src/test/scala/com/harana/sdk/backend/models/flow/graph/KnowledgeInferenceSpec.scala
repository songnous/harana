package com.harana.sdk.backend.models.flow.graph

import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.shared.models.flow.graph.FlowGraph.FlowNode
import com.harana.sdk.shared.models.designer.flow.graph._
import com.harana.sdk.shared.models.flow.exceptions.CyclicGraphError
import com.harana.sdk.shared.models.flow.graph.{Edge, GraphAction, TopologicallySortable}
import com.harana.sdk.shared.models.flow.graph.node.Node
import org.mockito.ArgumentMatchers.{any, eq => isEqualTo}
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfter

class KnowledgeInferenceSpec extends AbstractInferenceSpec with BeforeAndAfter {

  val topologicallySortedMock = mock[TopologicallySortable[GraphAction]]
  val nodeInferenceMock = mock[NodeInference]
  val graph = DirectedGraphWithSomeLogicMocked(topologicallySortedMock, nodeInferenceMock)

  before {
    reset(topologicallySortedMock)
    reset(nodeInferenceMock)
  }

  "Graph" should {

    "infer type knowledge" when {

      "graph is valid" in {
        val topologicallySortedNodes = List(
          nodeCreateA1,
          nodeA1ToA,
          nodeAToA1A2,
          nodeA1A2ToFirst
        )
        val nodeInferenceResultForNodes = List(
          NodeInferenceResult(Vector(knowledgeA1)),
          NodeInferenceResult(Vector(knowledgeA1, knowledgeA2)),
          NodeInferenceResult(Vector(knowledgeA1)),
          NodeInferenceResult(Vector(knowledgeA1), warnings = mock[InferenceWarnings])
        )
        when(topologicallySortedMock.topologicallySorted).thenReturn(Some(topologicallySortedNodes))
        topologicallySortedNodes.zip(nodeInferenceResultForNodes).foreach {
          case (node: FlowNode, result: NodeInferenceResult) => nodeInferenceMockShouldInferResultForNode(node, result)
        }

        val graphKnowledge = graph.inferKnowledge(mock[InferContext], GraphKnowledge())
        val graphKnowledgeExpected = topologicallySortedNodes
          .map(_.id)
          .zip(nodeInferenceResultForNodes)
          .toMap
        graphKnowledge.resultsMap should contain theSameElementsAs graphKnowledgeExpected
      }
    }

    "infer only unprovided knowledge" when {

      "given initial knowledge" in {

        val initialKnowledge = Map(
          nodeCreateA1.id -> NodeInferenceResult(Vector(knowledgeA1)),
          nodeA1ToA.id -> NodeInferenceResult(Vector(knowledgeA1, knowledgeA2))
        )

        val knowledgeToInfer = Map(
          nodeAToA1A2.id -> NodeInferenceResult(Vector(knowledgeA1)),
          nodeA1A2ToFirst.id -> NodeInferenceResult(Vector(knowledgeA1), warnings = mock[InferenceWarnings])
        )

        val nodesWithKnowledge  = List(nodeCreateA1, nodeA1ToA)
        val nodesToInfer = List(nodeAToA1A2, nodeA1A2ToFirst)
        val topologicallySorted = nodesWithKnowledge ++ nodesToInfer

        when(topologicallySortedMock.topologicallySorted).thenReturn(Some(topologicallySorted))

        nodesWithKnowledge.foreach((node: FlowNode) => nodeInferenceMockShouldThrowForNode(node))
        nodesToInfer.foreach((node: FlowNode) => nodeInferenceMockShouldInferResultForNode(node, knowledgeToInfer(node.id)))

        val expectedKnowledge = initialKnowledge ++ knowledgeToInfer
        val graphKnowledge = graph.inferKnowledge(mock[InferContext], GraphKnowledge(initialKnowledge))

        graphKnowledge.resultsMap should contain theSameElementsAs expectedKnowledge
      }
    }

    "throw an exception" when {

      "graph contains cycle" in {
        intercept[CyclicGraphError] {
          val topologicallySortedMock = mock[TopologicallySortable[GraphAction]]
          when(topologicallySortedMock.topologicallySorted).thenReturn(None)
          val graph = DirectedGraphWithSomeLogicMocked(topologicallySortedMock, nodeInferenceMock)
          graph.inferKnowledge(mock[InferContext], GraphKnowledge())
        }
        ()
      }
    }
  }

  def nodeInferenceMockShouldInferResultForNode(nodeCreateA1: FlowNode, nodeCreateA1InferenceResult: NodeInferenceResult) =
    when(nodeInferenceMock.inferKnowledge(isEqualTo(nodeCreateA1), any[InferContext], any[NodeInferenceResult])).thenReturn(nodeCreateA1InferenceResult)

  def nodeInferenceMockShouldThrowForNode(node: FlowNode) =
    when(nodeInferenceMock.inferKnowledge(isEqualTo(node), any[InferContext], any[NodeInferenceResult]))
      .thenThrow(new RuntimeException("Inference should not be called for node " + node.id))

  case class DirectedGraphWithSomeLogicMocked(topologicallySortableMock: TopologicallySortable[GraphAction], nodeInferenceMock: NodeInference) extends TopologicallySortable[GraphAction]
      with KnowledgeInference
      with NodeInference {

    override def inferKnowledge(node: FlowNode, context: InferContext, inputInferenceForNode: NodeInferenceResult) =
      nodeInferenceMock.inferKnowledge(node, context, inputInferenceForNode)

    def inputInferenceForNode(node: FlowNode, context: InferContext, graphKnowledge: GraphKnowledge, nodePredecessorsEndpoints: IndexedSeq[Option[Endpoint]]) = {
      nodeInferenceMock.inputInferenceForNode(
        node,
        context,
        graphKnowledge,
        nodePredecessorsEndpoints
      )
    }

    def topologicallySorted = topologicallySortableMock.topologicallySorted

    def node(id: Node.Id): FlowNode = topologicallySortableMock.node(id)

    def allPredecessorsOf(id: Node.Id): Set[FlowNode] = topologicallySortableMock.allPredecessorsOf(id)
    def predecessors(id: Node.Id) = topologicallySortableMock.predecessors(id)
    def successors(id: Node.Id) = topologicallySortableMock.successors(id)

    def edges: Set[Edge] = ???
    def nodes: Set[FlowNode] = ???

  }
}
