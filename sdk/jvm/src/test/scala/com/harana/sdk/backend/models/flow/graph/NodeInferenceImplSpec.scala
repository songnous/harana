package com.harana.sdk.backend.models.flow.graph

import com.harana.sdk.backend.models.flow.{Knowledge, graph}
import com.harana.sdk.backend.models.flow.graph.DClassesForActions.A1
import com.harana.sdk.backend.models.flow.inference.InferenceWarnings
import com.harana.sdk.backend.models.flow.inference.exceptions.{AllTypesNotCompilableError, NoInputEdgesError}
import com.harana.sdk.backend.models.flow.inference.warnings.SomeTypesNotCompilableWarning
import com.harana.sdk.backend.models.flow.Knowledge
import com.harana.sdk.shared.models.designer.flow.graph.Endpoint
import com.harana.sdk.shared.models.flow.ActionObjectInfo
import com.harana.sdk.shared.models.flow.graph.node.Node

class NodeInferenceImplSpec extends AbstractInferenceSpec {

  val nodeInference = new NodeInference {}

  "inputInferenceForNode" should {

    "return empty inference for node without input" in {
      val inferenceResult = nodeInference.inputInferenceForNode(nodeCreateA1, inferenceCtx, GraphKnowledge(), IndexedSeq())
      inferenceResult shouldBe NodeInferenceResult.empty
    }

    "return correct inference" in {
      val inferenceResult = testInputInferenceForNode(0, nodeAToA1A2, Vector(knowledgeA1))
      inferenceResult shouldBe graph.NodeInferenceResult(Vector(knowledgeA1))
    }

    "return inference with warnings when not all types are compatible" in {
      val inferenceResult = testInputInferenceForNode(0, nodeA1ToA, Vector(knowledgeA12))
      inferenceResult shouldBe NodeInferenceResult(
        Vector(Knowledge(A1())),
        warnings = InferenceWarnings(
          SomeTypesNotCompilableWarning(portIndex = 0)
        )
      )
    }

    "return inference with error when types not compatible" in {
      val inferenceResult = testInputInferenceForNode(0, nodeA1ToA, Vector(knowledgeA2))
      inferenceResult shouldBe NodeInferenceResult(
        Vector(Knowledge(A1())),
        errors = Vector(AllTypesNotCompilableError(portIndex = 0))
      )
    }

    "return default knowledge with errors when missing inference for input (missing edges)" in {
      val nodePredecessorsEndpoints = IndexedSeq(None, None)
      val inferenceResult = nodeInference.inputInferenceForNode(nodeA1A2ToFirst, inferenceCtx, GraphKnowledge(), nodePredecessorsEndpoints)
      inferenceResult shouldBe graph.NodeInferenceResult(
        Vector(knowledgeA1, knowledgeA2),
        errors = Vector(NoInputEdgesError(0), NoInputEdgesError(1))
      )
    }

    "return default knowledge with errors when missing inference for one input and invalid type for other" in {
        val predecessorId = Node.Id.randomId
        val nodePredecessorsEndpoints = IndexedSeq(None, Some(Endpoint(predecessorId, 0)))
        val graphKnowledge = GraphKnowledge(Map(predecessorId -> graph.NodeInferenceResult(Vector(knowledgeA1))))
        val inferenceResult = nodeInference.inputInferenceForNode(nodeA1A2ToFirst, inferenceCtx, graphKnowledge, nodePredecessorsEndpoints)
        inferenceResult shouldBe NodeInferenceResult(Vector(knowledgeA1, knowledgeA2), errors = Vector(NoInputEdgesError(0), AllTypesNotCompilableError(1)))
      }
  }
  "inferKnowledge" should {

    "return correct knowledge" in {
      val node = nodeA1A2ToFirst
      setParametersValid(node)
      val inputInferenceForNode = NodeInferenceResult(Vector(knowledgeA1, knowledgeA2))
      val inferenceResult = nodeInference.inferKnowledge(node, inferenceCtx, inputInferenceForNode)
      inferenceResult shouldBe NodeInferenceResult(Vector(knowledgeA1), warnings = InferenceWarnings(ActionA1A2ToFirst.warning))
    }

    "not infer types and return default knowledge with validation errors when parameters are not valid" in {
      val node = nodeA1A2ToFirst
      setParametersInvalid(node)
      val inputInferenceForNode = NodeInferenceResult(Vector(knowledgeA1, knowledgeA2))
      val inferenceResult = nodeInference.inferKnowledge(node, inferenceCtx, inputInferenceForNode)
      inferenceResult shouldBe NodeInferenceResult(Vector(knowledgeA12), errors = Vector(ActionA1A2ToFirst.parameterInvalidError))
    }

    "return default knowledge when node inference throws an error" in {
      val node = nodeA1A2ToFirst
      setInferenceErrorThrowing(node)
      setParametersValid(node)
      val inputInferenceForNode = NodeInferenceResult(Vector(knowledgeA1, knowledgeA2))
      val inferenceResult       = nodeInference.inferKnowledge(node, inferenceCtx, inputInferenceForNode)
      inferenceResult shouldBe NodeInferenceResult(
        Vector(knowledgeA12),
        errors = Vector(ActionA1A2ToFirst.inferenceError)
      )
    }

    "skip duplicated errors" in {
      val node = nodeA1A2ToFirst
      setInferenceErrorThrowing(node)
      setParametersInvalid(node)
      val inputInferenceForNode = graph.NodeInferenceResult(
        ports = Vector(knowledgeA1, knowledgeA2),
        errors = Vector(ActionA1A2ToFirst.parameterInvalidError, ActionA1A2ToFirst.inferenceError)
      )
      val inferenceResult = nodeInference.inferKnowledge(node, inferenceCtx, inputInferenceForNode)
      inferenceResult shouldBe graph.NodeInferenceResult(
        Vector(knowledgeA12),
        errors = Vector(
          ActionA1A2ToFirst.parameterInvalidError,
          ActionA1A2ToFirst.inferenceError
        )
      )
    }
    "handle DeepLangMultiException" in {
      val node = nodeA1A2ToFirst
      setInferenceErrorMultiThrowing(node)
      val inputInferenceForNode = graph.NodeInferenceResult(
        ports = Vector(knowledgeA1, knowledgeA2),
        errors = Vector(ActionA1A2ToFirst.parameterInvalidError)
      )
      val inferenceResult = nodeInference.inferKnowledge(node, inferenceCtx, inputInferenceForNode)
      inferenceResult shouldBe graph.NodeInferenceResult(Vector(knowledgeA12), errors = Vector(ActionA1A2ToFirst.parameterInvalidError))
    }
  }

  def testInputInferenceForNode(predecessorPortIndex: Int, node: FlowNode, predecessorKnowledge: Vector[Knowledge[ActionObjectInfo]]) = {
    val predecessorId = Node.Id.randomId
    val nodePredecessorsEndpoints = IndexedSeq(Some(Endpoint(predecessorId, predecessorPortIndex)))
    val graphKnowledge = GraphKnowledge(Map(predecessorId -> graph.NodeInferenceResult(predecessorKnowledge)))
    val inferenceResult = nodeInference.inputInferenceForNode(node, inferenceCtx, graphKnowledge, nodePredecessorsEndpoints)
    inferenceResult
  }
}
