package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.flow.{Knowledge, TestSupport, UnitSpec}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actions.MockActionObjectsFactory.{MockEstimator, MockEvaluator}
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.shared.models.flow.actionobjects.report.Report
import com.harana.sdk.shared.models.flow.actions.GridSearchInfo
import com.harana.sdk.shared.models.flow.exceptions.FlowMultiError
import io.circe.Json
import io.circe.syntax.EncoderOps

class GridSearchSpec extends UnitSpec with TestSupport {

  "GridSearch" should {

    "infer knowledge when dynamic parameters are valid" in {
      val inputDF   = DataFrame.forInference(createSchema())
      val estimator = new MockEstimator
      val evaluator = new MockEvaluator

      val gridSearch = new GridSearch()
      gridSearch.inferKnowledgeUntyped(List(Knowledge(estimator), Knowledge(inputDF), Knowledge(evaluator)))(
        mock[InferContext]
      ) shouldBe
        (List(Knowledge(Report())), InferenceWarnings.empty)
    }

    "throw Exception" when {

      "Estimator's dynamic parameters are invalid" in {
        checkMultiException(Some(-2), None)
      }

      "Evaluator's dynamic parameters are invalid" in {
        checkMultiException(None, Some(-2))
      }

      "Both Estimator's and Evaluator's dynamic parameters are invalid" in {
        checkMultiException(Some(-2), Some(-2))
      }
    }
  }

  private def checkMultiException(estimatorParamValue: Option[Double], evaluatorParamValue: Option[Double]) = {
    val inputDF = DataFrame.forInference(createSchema())
    val estimator = new MockEstimator
    val evaluator = new MockEvaluator

    val gridSearch = new GridSearch()
      .setEstimatorParameters(prepareParamDictionary(estimator.paramA.name, estimatorParamValue))
      .setEvaluatorParameters(prepareParamDictionary(evaluator.paramA.name, evaluatorParamValue))

    val multiException = the[FlowMultiError] thrownBy {
      gridSearch.inferKnowledgeUntyped(List(Knowledge(estimator), Knowledge(inputDF), Knowledge(evaluator)))(
        mock[InferContext]
      )
    }

    val invalidParamCount = estimatorParamValue.map(_ => 1).getOrElse(0) + evaluatorParamValue.map(_ => 1).getOrElse(0)
    multiException.exceptions should have size invalidParamCount
  }

  private def prepareParamDictionary(parameterName: String, maybeValue: Option[Double]): Json =
    maybeValue
      .map(value => Seq(parameterName -> value))
      .getOrElse(Seq())
      .asJson
}
