package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.flow._
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actions.MockActionObjectsFactory._
import com.harana.sdk.backend.models.flow.actions.exceptions.TooManyPossibleTypesError
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.flow.parameters.ParametersMatchers.theSameParametersAs
import com.harana.sdk.backend.models.flow.Knowledge
import com.harana.sdk.backend.models.flow.actions.exceptions.TooManyPossibleTypesError
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.shared.models.flow.ActionObjectInfo
import com.harana.sdk.shared.models.flow.actionobjects.MetricValue
import com.harana.sdk.shared.models.flow.exceptions.FlowMultiError
import io.circe.syntax._

class EvaluateSpec extends UnitSpec with TestSupport {

  "Evaluate" should {

    "evaluate input Evaluator on input DataFrame with proper parameters set" in {
      val evaluator = new MockEvaluator

      def testEvaluate(op: Evaluate, expected: MetricValue) = {
        val List(outputDataFrame) = op.executeUntyped(List(evaluator, mock[DataFrame]))(createExecutionContext)
        outputDataFrame shouldBe expected
      }

      val op1 = new Evaluate()
      testEvaluate(op1, metricValue1)

      val parametersForEvaluator = Map(evaluator.paramA.name -> 2).asJson
      val op2 = new Evaluate().setEvaluatorParameters(parametersForEvaluator)
      testEvaluate(op2, metricValue2)
    }

    "not modify parameters in input Evaluator instance upon execution" in {
      val evaluator = new MockEvaluator
      val originalEvaluator = evaluator.replicate()

      val parametersForEvaluator = Map(evaluator.paramA.name -> 2).asJson
      val op = new Evaluate().setEvaluatorParameters(parametersForEvaluator)
      op.executeUntyped(List(evaluator, mock[DataFrame]))(createExecutionContext)

      evaluator should have(theSameParametersAs(originalEvaluator))
    }

    "infer knowledge from input Evaluator on input DataFrame with proper parameters set" in {
      val evaluator = new MockEvaluator

      def testInference(op: Evaluate, expectedKnowledge: Knowledge[MetricValue]) = {
        val inputDF = DataFrame.forInference(createSchema())
        val (knowledge, warnings) = op.inferKnowledgeUntyped(List(Knowledge(evaluator), Knowledge(inputDF)))(mock[InferContext])
        // Currently, InferenceWarnings are always empty.
        warnings shouldBe InferenceWarnings.empty
        val List(dataFrameKnowledge) = knowledge
        dataFrameKnowledge shouldBe expectedKnowledge
      }

      val op1 = new Evaluate()
      testInference(op1, metricValueKnowledge1)

      val parametersForEvaluator = Map(evaluator.paramA.name -> 2).asJson
      val op2 = new Evaluate().setEvaluatorParameters(parametersForEvaluator)
      testInference(op2, metricValueKnowledge2)
    }

    "not modify parameters in input Evaluator instance upon inference" in {
      val evaluator = new MockEvaluator
      val originalEvaluator = evaluator.replicate()

      val parametersForEvaluator = Map(evaluator.paramA.name -> 2).asJson
      val op = new Evaluate().setEvaluatorParameters(parametersForEvaluator)
      val inputDF = DataFrame.forInference(createSchema())
      op.inferKnowledgeUntyped(List(Knowledge(evaluator), Knowledge(inputDF)))(mock[InferContext])

      evaluator should have(theSameParametersAs(originalEvaluator))
    }

    "throw Exception" when {
      "there is more than one Evaluator in input Knowledge" in {
        val inputDF = DataFrame.forInference(createSchema())
        val evaluators = Set[ActionObjectInfo](new MockEvaluator, new MockEvaluator)

        val op = new Evaluate()
        a[TooManyPossibleTypesError] shouldBe thrownBy {
          op.inferKnowledgeUntyped(List(Knowledge(evaluators), Knowledge(inputDF)))(mock[InferContext])
        }
      }
      "values of dynamic parameters are invalid" in {
        val evaluator = new MockEvaluator
        val inputDF = DataFrame.forInference(createSchema())

        val parametersForEvaluator = Map(evaluator.paramA.name -> -2).asJson
        val evaluatorWithParameters = new Evaluate().setEvaluatorParameters(parametersForEvaluator)

        a[FlowMultiError] shouldBe thrownBy {
          evaluatorWithParameters.inferKnowledgeUntyped(List(Knowledge(evaluator), Knowledge(inputDF)))(
            mock[InferContext]
          )
        }
      }
    }
  }
}
