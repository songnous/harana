package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.evaluators

import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.actionobjects.SparkEvaluatorWrapperInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.{HasLabelColumnParameter, HasPredictionColumnSelectorParameter}
import com.harana.sdk.shared.models.flow.parameters.Parameter
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}

trait MulticlassClassificationEvaluatorInfo
    extends SparkEvaluatorWrapperInfo
    with HasPredictionColumnSelectorParameter
    with HasLabelColumnParameter {

  import MulticlassClassificationEvaluatorInfo._

  val id = "45F4CAB1-954A-4CD2-AF83-E2883CE30CE4"

  val metricNameParameter = ChoiceParameter[Metric]("multiclass metric", Some("The metric used in evaluation."))
  setDefault(metricNameParameter, F1())
  def getMetricName = $(metricNameParameter).name

  val parameters = Array(metricNameParameter, predictionColumnParameter, labelColumnParameter)
}

object MulticlassClassificationEvaluatorInfo extends MulticlassClassificationEvaluatorInfo {
  sealed abstract class Metric(val name: String) extends Choice {
    val choiceOrder: List[ChoiceOption] = List(classOf[F1], classOf[Precision], classOf[Recall], classOf[WeightedPrecision], classOf[WeightedRecall])
    val parameters = Array.empty[Parameter[_]]
  }

  case class F1() extends Metric("f1")
  case class Precision() extends Metric("precision")
  case class Recall() extends Metric("recall")
  case class WeightedPrecision() extends Metric("weightedPrecision")
  case class WeightedRecall() extends Metric("weightedRecall")
}