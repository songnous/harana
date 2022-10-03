package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow.actionobjects.Transformer
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actiontypes.MockActionObjectsFactory$Type._
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.flow._
import com.harana.sdk.shared.models.flow.parameters.ParameterMap
import com.harana.sdk.shared.models.flow.utils.Id
import org.apache.spark.sql.types.StructType

class EstimatorAsActionSpec$Type extends UnitSpec with TestSupport {

  import EstimatorAsActionSpecType._

  "EstimatorAsAction" should {
    def createMockAction: MockEstimatorActionType = new MockEstimatorActionType

    "have the same specific parameters as the Estimator" in {
      val op = createMockAction
      op.parameters shouldBe op.estimator.parameters
    }

    "have the same default values for parameters as Estimator" in {
      val op = createMockAction
      val estimatorAction = op.estimator.paramA -> DefaultForA
      op.extractParameterMap() shouldBe ParameterMap(estimatorAction, ReportTypeDefault(op.reportTypeParameter))
    }

    "execute fit using properly set parameters" in {
      def testFit(op: MockEstimatorActionType, expectedDataFrame: DataFrame, expectedTransformer: Transformer) = {
        val List(outputDataFrame: DataFrame, outputTransformer: Transformer) = op.executeUntyped(List(mock[DataFrame]))(mock[ExecutionContext])
        outputDataFrame shouldBe expectedDataFrame
        outputTransformer shouldBe expectedTransformer
      }

      val op = createMockAction
      testFit(op, transformedDataFrame1, transformer1)
      op.set(op.estimator.paramA -> 2)
      testFit(op, transformedDataFrame2, transformer2)
    }

    "infer types using properly set parameters" in {
      def testInference(op: MockEstimatorActionType, expectedSchema: StructType, expectedTransformerKnowledge: Knowledge[Transformer]) = {
        val inputDF = DataFrame.forInference(createSchema())
        val (knowledge, warnings) = op.inferKnowledgeUntyped(List(Knowledge(inputDF)))(mock[InferContext])

        // Warnings should be a sum of transformer inference warnings
        // and estimator inference warnings. Currently, either both of them
        // are empty or the inferences throw exception, so the sum is always 'empty'.
        warnings shouldBe InferenceWarnings.empty
        val List(dataFrameKnowledge, transformerKnowledge) = knowledge
        dataFrameKnowledge shouldBe Knowledge(DataFrame.forInference(expectedSchema))
        transformerKnowledge shouldBe expectedTransformerKnowledge
      }

      val op = createMockAction
      testInference(op, transformedDataFrameSchema1, transformerKnowledge1)
      op.set(op.estimator.paramA -> 2)
      testInference(op, transformedDataFrameSchema2, transformerKnowledge2)
    }
  }
}

object EstimatorAsActionSpecType extends UnitSpec {

  class MockEstimatorActionType extends EstimatorAsActionType[MockEstimator, Transformer] {
    val id: Id = Id.randomId
    val name = "Mock Estimator as an action"
    }
}
