package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.evaluators

import com.harana.sdk.backend.models.flow.actionobjects.AbstractEvaluatorSmokeTest
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.evaluators.RegressionEvaluatorInfo.Rmse
import com.harana.sdk.shared.models.flow.parameters.ParameterPair
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class RegressionEvaluatorSmokeTest extends AbstractEvaluatorSmokeTest {

  def className = "RegressionEvaluator"

  val evaluator: RegressionEvaluator = new RegressionEvaluator()

  val evaluatorParameters = Seq(
    evaluator.metricNameParameter       -> Rmse(),
    evaluator.predictionColumnParameter -> NameSingleColumnSelection("prediction"),
    evaluator.labelColumnParameter      -> NameSingleColumnSelection("label")
  )

}
