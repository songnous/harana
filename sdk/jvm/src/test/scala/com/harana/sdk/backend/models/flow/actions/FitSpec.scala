package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.flow._
import com.harana.sdk.backend.models.flow.actionobjects.Transformer
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actions.MockActionObjectsFactory._
import com.harana.sdk.backend.models.flow.actions.exceptions.TooManyPossibleTypesError
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.flow.parameters.ParametersMatchers.theSameParametersAs
import com.harana.sdk.backend.models.flow.Knowledge
import com.harana.sdk.backend.models.flow.actionobjects.Transformer
import com.harana.sdk.backend.models.flow.actions.exceptions.TooManyPossibleTypesError
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.shared.models.flow.ActionObjectInfo
import com.harana.sdk.shared.models.flow.exceptions.FlowMultiError
import io.circe.syntax._

class FitSpec extends UnitSpec with TestSupport {

  "Fit" should {

    "fit input Estimator on input DataFrame with proper parameters set" in {
      val estimator = new MockEstimator

      def testFit(op: Fit, expectedTransformer: Transformer) = {
        val Vector(outputTransformer: Transformer) = op.executeUntyped(Vector(estimator, mock[DataFrame]))(createExecutionContext)
        outputTransformer shouldBe expectedTransformer
      }
      val op1 = new Fit()
      testFit(op1, transformer1)

      val parametersForEstimator = Map(estimator.paramA.name -> 2).asJson
      val op2 = new Fit().setEstimatorParameters(parametersForEstimator)
      testFit(op2, transformer2)
    }

    "not modify parameters in input Estimator instance upon execution" in {
      val estimator = new MockEstimator
      val originalEstimator = estimator.replicate()
      val parametersForEstimator = Map(estimator.paramA.name -> 2).asJson
      val op = new Fit().setEstimatorParameters(parametersForEstimator)
      op.executeUntyped(Vector(estimator, mock[DataFrame]))(createExecutionContext)

      estimator should have(theSameParametersAs(originalEstimator))
    }

    "infer Transformer from input Estimator on input DataFrame with proper parameters set" in {
      val estimator = new MockEstimator

      def testInference(op: Fit, expectedTransformerKnowledge: Knowledge[Transformer]) = {
        val inputDF = DataFrame.forInference(createSchema())
        val (knowledge, warnings) = op.inferKnowledgeUntyped(Vector(Knowledge(estimator), Knowledge(inputDF)))(mock[InferContext])
        warnings shouldBe InferenceWarnings.empty
        val Vector(transformerKnowledge) = knowledge
        transformerKnowledge shouldBe expectedTransformerKnowledge
      }
      val op1 = new Fit()
      testInference(op1, transformerKnowledge1)

      val parametersForEstimator = Map(estimator.paramA.name -> 2).asJson
      val op2 = new Fit().setEstimatorParameters(parametersForEstimator)
      testInference(op2, transformerKnowledge2)
    }

    "not modify parameters in input Estimator instance upon inference" in {
      val estimator = new MockEstimator
      val originalEstimator = estimator.replicate()

      val parametersForEstimator = Map(estimator.paramA.name -> 2).asJson
      val op = new Fit().setEstimatorParameters(parametersForEstimator)
      val inputDF = DataFrame.forInference(createSchema())
      op.inferKnowledgeUntyped(Vector(Knowledge(estimator), Knowledge(inputDF)))(mock[InferContext])

      estimator should have(theSameParametersAs(originalEstimator))
    }

    "throw Exception" when {

      "there are more than one Estimator in input Knowledge" in {
        val inputDF = DataFrame.forInference(createSchema())
        val estimators = Set[ActionObjectInfo](new MockEstimator, new MockEstimator)

        val op = new Fit()
        a[TooManyPossibleTypesError] shouldBe thrownBy {
          op.inferKnowledgeUntyped(Vector(Knowledge(estimators), Knowledge(inputDF)))(mock[InferContext])
        }
      }

      "Estimator's dynamic parameters are invalid" in {
        val inputDF = DataFrame.forInference(createSchema())
        val estimator = new MockEstimator
        val fit = new Fit().setEstimatorParameters(Map(estimator.paramA.name -> -2).asJson)
        a[FlowMultiError] shouldBe thrownBy {
          fit.inferKnowledgeUntyped(Vector(Knowledge(estimator), Knowledge(inputDF)))(mock[InferContext])
        }
      }
    }
  }
}
