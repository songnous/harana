package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow._
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actiontypes.MockActionObjectsFactory$Type._
import com.harana.sdk.backend.models.flow.actiontypes.MockTransformers._
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.flow.parameters.ParametersMatchers.theSameParametersAs
import com.harana.sdk.shared.models.flow.actionobjects.ActionObjectInfo
import com.harana.sdk.shared.models.flow.exceptions.FlowMultiError
import io.circe.syntax.EncoderOps

class TransformSpec extends UnitSpec with TestSupport {

  "Transform" should {

    "transform input Transformer on input DataFrame with proper parameters set" in {
      val transformer = new MockTransformer

      def testTransform(op: Transform, expectedDataFrame: DataFrame) = {
        val List(outputDataFrame) = op.executeUntyped(List(transformer, createDataFrame()))(createExecutionContext)
        outputDataFrame shouldBe expectedDataFrame
      }

      val op1 = new Transform()
      testTransform(op1, outputDataFrame1)

      val parametersForTransformer = Map(transformer.paramA.name -> 2).asJson
      val op2 = new Transform().setTransformerParameters(parametersForTransformer)
      testTransform(op2, outputDataFrame2)
    }

    "not modify parameters in input Transformer instance upon execution" in {
      val transformer = new MockTransformer
      val originalTransformer = transformer.replicate()

      val parametersForTransformer = Map(transformer.paramA.name -> 2).asJson
      val op = new Transform().setTransformerParameters(parametersForTransformer)
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

      val op1 = new Transform()
      testInference(op1, dataFrameKnowledge(outputSchema1))

      val parametersForTransformer = Map(transformer.paramA.name -> 2).asJson
      val op2 = new Transform().setTransformerParameters(parametersForTransformer)
      testInference(op2, dataFrameKnowledge(outputSchema2))
    }

    "not modify parameters in input Transformer instance upon inference" in {
      val transformer = new MockTransformer
      val originalTransformer = transformer.replicate()

      val parametersForTransformer = Map(transformer.paramA.name -> 2).asJson
      val op = new Transform().setTransformerParameters(parametersForTransformer)
      val inputDF = DataFrame.forInference(createSchema())
      op.inferKnowledgeUntyped(List(Knowledge(transformer), Knowledge(inputDF)))(mock[InferContext])

      transformer should have(theSameParametersAs(originalTransformer))
    }

    "infer knowledge even if there is more than one Transformer in input Knowledge" in {
      val inputDF = DataFrame.forInference(createSchema())
      val transformers = Set[ActionObjectInfo](new MockTransformer, new MockTransformer)

      val op = new Transform()
      val (knowledge, warnings) = op.inferKnowledgeUntyped(List(Knowledge(transformers), Knowledge(inputDF)))(mock[InferContext])
      knowledge shouldBe List(Knowledge(DataFrame.forInference()))
      warnings shouldBe InferenceWarnings.empty
    }

    "throw Exception" when {
      "Transformer's dynamic parameters are invalid" in {
        val inputDF = DataFrame.forInference(createSchema())
        val transformer = new MockTransformer
        val transform = new Transform().setTransformerParameters(Map(transformer.paramA.name -> -2).asJson)

        a[FlowMultiError] shouldBe thrownBy {
          transform.inferKnowledgeUntyped(List(Knowledge(transformer), Knowledge(inputDF)))(mock[InferContext])
        }
      }
    }
  }
}