package com.harana.sdk.backend.models.flow.graph

import com.harana.sdk.shared.models.flow.exceptions.FlowError
import com.harana.sdk.shared.models.flow.graph.node.Node
import org.mockito.Mockito._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar

class GraphKnowledgeSpec extends AnyWordSpec with Matchers with MockitoSugar {

  "GraphKnowledge" should {

    "return proper errors map" in {
      val node1Id = Node.Id.randomId
      val node2Id = Node.Id.randomId
      val inferenceResultsWithErrors = mock[NodeInferenceResult]

      val errors = List(mock[FlowError], mock[FlowError])
      when(inferenceResultsWithErrors.errors).thenReturn(errors)

      val inferenceResultsWithoutErrors = mock[NodeInferenceResult]
      when(inferenceResultsWithoutErrors.errors).thenReturn(List.empty)

      val knowledge = GraphKnowledge()
        .addInference(node1Id, inferenceResultsWithErrors)
        .addInference(node2Id, inferenceResultsWithoutErrors)

      knowledge.errors shouldBe Map(node1Id -> errors)
    }
  }
}
