package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.evaluators

import RegressionEvaluatorInfo.{Metric, Rmse}
import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.actionobjects.EvaluatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.{HasLabelColumnParameter, HasPredictionColumnSelectorParameter}
import com.harana.sdk.shared.models.flow.parameters.Parameter
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}

trait RegressionEvaluatorInfo
  extends EvaluatorInfo
  with HasPredictionColumnSelectorParameter
  with HasLabelColumnParameter {

  val id = "721588DE-97CF-4B5D-9A4C-905E6580F235"

  val metricNameParameter = ChoiceParameter[Metric]("regression metric")
  setDefault(metricNameParameter, Rmse())
  def getMetricName = $(metricNameParameter).name

  val parameters = Array(metricNameParameter, predictionColumnParameter, labelColumnParameter)
}

object RegressionEvaluatorInfo extends RegressionEvaluatorInfo {

  sealed abstract class Metric(val name: String) extends Choice {
    val choiceOrder: List[ChoiceOption] = List(classOf[Mse], classOf[Rmse], classOf[R2], classOf[Mae])
    val parameters = Array.empty[Parameter[_]]
  }

  case class Mae() extends Metric("mae")
  case class Mse() extends Metric("mse")
  case class Rmse() extends Metric("rmse")
  case class R2() extends Metric("r2")
}

