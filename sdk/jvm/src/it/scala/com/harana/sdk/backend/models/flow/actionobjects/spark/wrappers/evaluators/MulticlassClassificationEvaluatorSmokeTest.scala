package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.evaluators

import com.harana.sdk.backend.models.flow.actionobjects.AbstractEvaluatorSmokeTest
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.evaluators.MulticlassClassificationEvaluatorInfo.F1
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator

class MulticlassClassificationEvaluatorSmokeTest extends AbstractEvaluatorSmokeTest {

  def className = "MulticlassClassificationEvaluator"

  val evaluator = new MulticlassClassificationEvaluator()

  val evaluatorParameters = Seq(
    evaluator.metricNameParameter       -> F1(),
    evaluator.predictionColumnParameter -> NameSingleColumnSelection("prediction"),
    evaluator.labelColumnParameter      -> NameSingleColumnSelection("label")
  )

}
