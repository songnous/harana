package com.harana.sdk.backend.models.flow.actions

import org.apache.spark.sql.types.StructType
import com.harana.sdk.backend.models.flow._
import com.harana.sdk.backend.models.flow.actionobjects._
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actions.MockActionObjectsFactory._
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.flow.{ExecutionContext, Knowledge}
import com.harana.sdk.backend.models.flow.actionobjects.Transformer
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.shared.models.flow.parameters.ParameterMap
import com.harana.sdk.shared.models.flow.utils.Id

class EstimatorAsActionSpec extends UnitSpec with TestSupport {

  import EstimatorAsActionSpec._

  "EstimatorAsAction" should {
    def createMockAction: MockEstimatorAction = new MockEstimatorAction

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
      def testFit(op: MockEstimatorAction, expectedDataFrame: DataFrame, expectedTransformer: Transformer) = {
        val Vector(outputDataFrame: DataFrame, outputTransformer: Transformer) = op.executeUntyped(Vector(mock[DataFrame]))(mock[ExecutionContext])
        outputDataFrame shouldBe expectedDataFrame
        outputTransformer shouldBe expectedTransformer
      }

      val op = createMockAction
      testFit(op, transformedDataFrame1, transformer1)
      op.set(op.estimator.paramA -> 2)
      testFit(op, transformedDataFrame2, transformer2)
    }

    "infer types using properly set parameters" in {
      def testInference(op: MockEstimatorAction, expectedSchema: StructType, expectedTransformerKnowledge: Knowledge[Transformer]) = {
        val inputDF = DataFrame.forInference(createSchema())
        val (knowledge, warnings) = op.inferKnowledgeUntyped(Vector(Knowledge(inputDF)))(mock[InferContext])

        // Warnings should be a sum of transformer inference warnings
        // and estimator inference warnings. Currently, either both of them
        // are empty or the inferences throw exception, so the sum is always 'empty'.
        warnings shouldBe InferenceWarnings.empty
        val Vector(dataFrameKnowledge, transformerKnowledge) = knowledge
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

object EstimatorAsActionSpec extends UnitSpec {

  class MockEstimatorAction extends EstimatorAsAction[MockEstimator, Transformer] {
    val id: Id = Id.randomId
    val name = "Mock Estimator as an action"
    }
}
