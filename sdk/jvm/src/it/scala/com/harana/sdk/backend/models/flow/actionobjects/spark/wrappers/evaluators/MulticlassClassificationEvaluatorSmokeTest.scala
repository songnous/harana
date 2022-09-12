package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.evaluators

import com.harana.sdk.backend.models.flow.actionobjects.AbstractEvaluatorSmokeTest
import com.harana.sdk.shared.models.flow.parameters.ParameterPair
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class MulticlassClassificationEvaluatorSmokeTest extends AbstractEvaluatorSmokeTest {

  def className = "MulticlassClassificationEvaluator"

  val evaluator: MulticlassClassificationEvaluator = new MulticlassClassificationEvaluator()

  val evaluatorParameters = Seq(
    evaluator.metricNameParameter       -> MulticlassClassificationEvaluator.F1(),
    evaluator.predictionColumnParameter -> NameSingleColumnSelection("prediction"),
    evaluator.labelColumnParameter      -> NameSingleColumnSelection("label")
  )

}
