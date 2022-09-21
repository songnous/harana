package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.flow._
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actions.MockActionObjectsFactory._
import com.harana.sdk.backend.models.flow.actions.MockTransformers._
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.flow.Knowledge
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.shared.models.flow.ActionObjectInfo
import com.harana.sdk.shared.models.flow.exceptions.FlowMultiError

class TransformSpec extends UnitSpec with TestSupport {

  "Transform" should {

    "transform input Transformer on input DataFrame with proper parameters set" in {
      val transformer = new MockTransformer

      def testTransform(op: Transform, expectedDataFrame: DataFrame) = {
        val List(outputDataFrame) = op.executeUntyped(List(transformer, createDataFrame()))(createExecutionContext)
        outputDataFrame shouldBe expectedDataFrame
      }

      val op1 = Transform()
      testTransform(op1, outputDataFrame1)

      val parametersForTransformer = Json(transformer.paramA.name -> 2)
      val op2 = Transform().setTransformerParameters(parametersForTransformer)
      testTransform(op2, outputDataFrame2)
    }

    "not modify parameters in input Transformer instance upon execution" in {
      val transformer = new MockTransformer
      val originalTransformer = transformer.replicate()

      val parametersForTransformer = Json(transformer.paramA.name -> 2)
      val op = Transform().setTransformerParameters(parametersForTransformer)
      op.executeUntyped(List(transformer, mock[DataFrame]))(createExecutionContext)

      transformer should have(theSameParametersAs(originalTransformer))
    }

    "infer knowledge from input Transformer on input DataFrame with proper parameters set" in {
      val transformer = new MockTransformer

      def testInference(op: Transform, expecteDataFrameKnowledge: Knowledge[DataFrame]) = {
        val inputDF = createDataFrame()
        val (knowledge, warnings)  = op.inferKnowledgeUntyped(List(Knowledge(transformer), Knowledge(inputDF)))(mock[InferContext])
        warnings shouldBe InferenceWarnings.empty
        val List(dataFrameKnowledge) = knowledge
        dataFrameKnowledge shouldBe expecteDataFrameKnowledge
      }

      val op1 = Transform()
      testInference(op1, dataFrameKnowledge(outputSchema1))

      val parametersForTransformer = Json(transformer.paramA.name -> 2)
      val op2 = Transform().setTransformerParameters(parametersForTransformer)
      testInference(op2, dataFrameKnowledge(outputSchema2))
    }

    "not modify parameters in input Transformer instance upon inference" in {
      val transformer = new MockTransformer
      val originalTransformer = transformer.replicate()

      val parametersForTransformer = Json(transformer.paramA.name -> 2)
      val op = Transform().setTransformerParameters(parametersForTransformer)
      val inputDF = DataFrame.forInference(createSchema())
      op.inferKnowledgeUntyped(List(Knowledge(transformer), Knowledge(inputDF)))(mock[InferContext])

      transformer should have(theSameParametersAs(originalTransformer))
    }

    "infer knowledge even if there is more than one Transformer in input Knowledge" in {
      val inputDF = DataFrame.forInference(createSchema())
      val transformers = Set[ActionObjectInfo](new MockTransformer, new MockTransformer)

      val op = Transform()
      val (knowledge, warnings) = op.inferKnowledgeUntyped(List(Knowledge(transformers), Knowledge(inputDF)))(mock[InferContext])
      knowledge shouldBe List(Knowledge(DataFrame.forInference()))
      warnings shouldBe InferenceWarnings.empty
    }

    "throw Exception" when {
      "Transformer's dynamic parameters are invalid" in {
        val inputDF = DataFrame.forInference(createSchema())
        val transformer = new MockTransformer
        val transform = Transform().setTransformerParameters(Json(transformer.paramA.name -> -2))

        a[FlowMultiError] shouldBe thrownBy {
          transform.inferKnowledgeUntyped(List(Knowledge(transformer), Knowledge(inputDF)))(mock[InferContext])
        }
      }
    }
  }
}
