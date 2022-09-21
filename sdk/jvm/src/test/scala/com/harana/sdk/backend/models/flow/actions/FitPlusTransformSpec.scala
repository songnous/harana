package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.flow.actionobjects.Transformer
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actions.MockActionObjectsFactory._
import com.harana.sdk.backend.models.flow.actions.exceptions.TooManyPossibleTypesError
import com.harana.sdk.backend.models.flow._
import com.harana.sdk.backend.models.flow.inference.InferContext
import com.harana.sdk.backend.models.flow.Knowledge
import com.harana.sdk.backend.models.flow.actionobjects.Transformer
import com.harana.sdk.backend.models.flow.actions.exceptions.TooManyPossibleTypesError
import com.harana.sdk.backend.models.flow.inference.InferContext
import com.harana.sdk.shared.models.flow.ActionObjectInfo
import com.harana.sdk.shared.models.flow.exceptions.FlowMultiError
import io.circe.syntax._

class FitPlusTransformSpec extends UnitSpec with TestSupport {

  "FitPlusTransform" when {
    "executed" should {

      "pass parameters to the input Estimator produce a Transformer and transformed DataFrame" in {
        val estimator = new MockEstimator
        val initialParametersValues = estimator.extractParameterMap()
        val fpt = new FitPlusTransform

        def testExecute(op: FitPlusTransform, expectedDataFrame: DataFrame, expectedTransformer: Transformer) = {
          val results = op.executeUntyped(List(estimator, mock[DataFrame]))(createExecutionContext)
          val outputDataFrame = results(0).asInstanceOf[DataFrame]
          val outputTransformer = results(1).asInstanceOf[Transformer]

          outputDataFrame shouldBe expectedDataFrame
          outputTransformer shouldBe expectedTransformer
        }

        testExecute(fpt, transformedDataFrame1, transformer1)
        fpt.setEstimatorParameters(Map(estimator.paramA.name -> 2).asJson)
        testExecute(fpt, transformedDataFrame2, transformer2)
        estimator.extractParameterMap() shouldBe initialParametersValues
      }
    }

    "inferring knowledge" should {

      "take parameters from the input Estimator, infer Transformer and then a DataFrame" in {
        val estimator = new MockEstimator
        val initialParametersValues = estimator.extractParameterMap()
        val fpt = new FitPlusTransform

        def testInference(op: FitPlusTransform, expectedDataFrameKnowledge: Knowledge[DataFrame], expectedTransformerKnowledge: Knowledge[Transformer]) = {
          val (List(outputDataFrameKnowledge, outputTransformerKnowledge), _) =
            op.inferKnowledgeUntyped(List(Knowledge(estimator), mock[Knowledge[DataFrame]]))(mock[InferContext])

          outputDataFrameKnowledge shouldBe expectedDataFrameKnowledge
          outputTransformerKnowledge shouldBe expectedTransformerKnowledge
        }

        testInference(fpt, transformedDataFrameKnowledge1, transformerKnowledge1)
        fpt.setEstimatorParameters(Map(estimator.paramA.name -> 2).asJson)
        testInference(fpt, transformedDataFrameKnowledge2, transformerKnowledge2)
        estimator.extractParameterMap() shouldBe initialParametersValues
      }
      "throw exceptions" when {

        "input Estimator Knowledge consist more than one type" in {
          val estimators = Set[ActionObjectInfo](new MockEstimator, new MockEstimator)
          val inputKnowledge: List[Knowledge[ActionObjectInfo]] = List(Knowledge(estimators), mock[Knowledge[DataFrame]])
          val fpt = new FitPlusTransform
          a[TooManyPossibleTypesError] shouldBe thrownBy {
            fpt.inferKnowledgeUntyped(inputKnowledge)(mock[InferContext])
          }
        }

        "Estimator's dynamic parameters are invalid" in {
          val estimator = new MockEstimator
          val inputKnowledge = List(Knowledge(estimator), mock[Knowledge[DataFrame]])
          val fpt = new FitPlusTransform
          fpt.setEstimatorParameters(Map(estimator.paramA.name -> -2).asJson)
          a[FlowMultiError] shouldBe thrownBy {
            fpt.inferKnowledgeUntyped(inputKnowledge)(mock[InferContext])
          }
        }
      }
    }
  }
}
