package com.harana.sdk.backend.models.flow.graph

import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.shared.models.flow.ActionTypeInfo
import com.harana.sdk.shared.models.flow.exceptions.CyclicGraphError
import com.harana.sdk.shared.models.flow.graph.node.Node
import com.harana.sdk.shared.models.flow.graph.{Edge, GraphAction, TopologicallySortable}
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
          NodeInferenceResult(List(knowledgeA1)),
          NodeInferenceResult(List(knowledgeA1, knowledgeA2)),
          NodeInferenceResult(List(knowledgeA1)),
          NodeInferenceResult(List(knowledgeA1), warnings = mock[InferenceWarnings])
        )
        when(topologicallySortedMock.topologicallySorted).thenReturn(Some(topologicallySortedNodes))
        topologicallySortedNodes.zip(nodeInferenceResultForNodes).foreach {
          case (node: Node[ActionTypeInfo], result: NodeInferenceResult) => nodeInferenceMockShouldInferResultForNode(node, result)
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
          nodeCreateA1.id -> NodeInferenceResult(List(knowledgeA1)),
          nodeA1ToA.id -> NodeInferenceResult(List(knowledgeA1, knowledgeA2))
        )

        val knowledgeToInfer = Map(
          nodeAToA1A2.id -> NodeInferenceResult(List(knowledgeA1)),
          nodeA1A2ToFirst.id -> NodeInferenceResult(List(knowledgeA1), warnings = mock[InferenceWarnings])
        )

        val nodesWithKnowledge  = List(nodeCreateA1, nodeA1ToA)
        val nodesToInfer = List(nodeAToA1A2, nodeA1A2ToFirst)
        val topologicallySorted = nodesWithKnowledge ++ nodesToInfer

        when(topologicallySortedMock.topologicallySorted).thenReturn(Some(topologicallySorted))

        nodesWithKnowledge.foreach((node: Node[ActionTypeInfo]) => nodeInferenceMockShouldThrowForNode(node))
        nodesToInfer.foreach((node: Node[ActionTypeInfo]) => nodeInferenceMockShouldInferResultForNode(node, knowledgeToInfer(node.id)))

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

  def nodeInferenceMockShouldInferResultForNode(nodeCreateA1: Node[ActionTypeInfo], nodeCreateA1InferenceResult: NodeInferenceResult) =
    when(nodeInferenceMock.inferKnowledge(isEqualTo(nodeCreateA1), any[InferContext], any[NodeInferenceResult])).thenReturn(nodeCreateA1InferenceResult)

  def nodeInferenceMockShouldThrowForNode(node: Node[ActionTypeInfo]) =
    when(nodeInferenceMock.inferKnowledge(isEqualTo(node), any[InferContext], any[NodeInferenceResult]))
      .thenThrow(new RuntimeException("Inference should not be called for node " + node.id))

  case class DirectedGraphWithSomeLogicMocked(topologicallySortableMock: TopologicallySortable[GraphAction], nodeInferenceMock: NodeInference) extends TopologicallySortable[GraphAction]
      with KnowledgeInference
      with NodeInference {

    override def inferKnowledge(node: Node[ActionTypeInfo], context: InferContext, inputInferenceForNode: NodeInferenceResult) =
      nodeInferenceMock.inferKnowledge(node, context, inputInferenceForNode)

    def inputInferenceForNode(node: Node[ActionTypeInfo], context: InferContext, graphKnowledge: GraphKnowledge, nodePredecessorsEndpoints: IndexedSeq[Option[Endpoint]]) = {
      nodeInferenceMock.inputInferenceForNode(
        node,
        context,
        graphKnowledge,
        nodePredecessorsEndpoints
      )
    }

    def topologicallySorted = topologicallySortableMock.topologicallySorted

    def node(id: Node.Id): Node[ActionTypeInfo] = topologicallySortableMock.node(id)

    def allPredecessorsOf(id: Node.Id): Set[Node[ActionTypeInfo]] = topologicallySortableMock.allPredecessorsOf(id)
    def predecessors(id: Node.Id) = topologicallySortableMock.predecessors(id)
    def successors(id: Node.Id) = topologicallySortableMock.successors(id)

    def edges: Set[Edge] = ???
    def nodes: Set[Node[ActionTypeInfo]] = ???

  }
}
