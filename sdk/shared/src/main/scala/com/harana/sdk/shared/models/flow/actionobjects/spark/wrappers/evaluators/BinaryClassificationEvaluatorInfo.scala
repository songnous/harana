package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.evaluators

import com.harana.sdk.shared.models.flow.actionobjects.EvaluatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.evaluators.BinaryClassificationEvaluatorInfo.{AreaUnderROC, Metric}
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.HasLabelColumnParameter
import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.selections.{NameSingleColumnSelection, SingleColumnSelection}
import com.harana.sdk.shared.models.flow.parameters.{ParameterGroup, Parameters, SingleColumnSelectorParameter}

trait BinaryClassificationEvaluatorInfo extends EvaluatorInfo with HasLabelColumnParameter {

  val id = "AEC7A0F2-9916-439A-A854-D063B39CD1AF"

  val metricNameParameter = new ChoiceParameter[Metric]("binary-metric", default = Some(AreaUnderROC()))
  def getMetricName = $(metricNameParameter)
  def setMetricName(value: Metric): this.type = set(metricNameParameter, value)

  override val parameterGroups = List(ParameterGroup("", metricNameParameter, labelColumnParameter))

  def isLargerBetter: Boolean = true
}

object BinaryClassificationEvaluatorInfo extends BinaryClassificationEvaluatorInfo {
  val areaUnderROC = "Area under ROC"
  val areaUnderPR = "Area under PR"
  val precision = "Precision"
  val recall = "Recall"
  val f1Score = "F1 Score"

  sealed abstract class Metric(name: String) extends Choice {
    val choiceOrder: List[ChoiceOption] = List(
      classOf[AreaUnderROC],
      classOf[AreaUnderPR],
      classOf[Precision],
      classOf[Recall],
      classOf[F1Score]
    )
  }

  trait RawPredictionMetric extends Parameters {
    val rawPredictionColumnParameter = SingleColumnSelectorParameter("raw-prediction-column", default = Some(NameSingleColumnSelection("rawPrediction")), portIndex = 0)
    def getRawPredictionColumn = $(rawPredictionColumnParameter)
    def setRawPredictionColumn(value: SingleColumnSelection): this.type = set(rawPredictionColumnParameter, value)
  }

  trait PredictionMetric extends Parameters {
    val predictionColumnParameter = SingleColumnSelectorParameter("prediction-column", default = Some(NameSingleColumnSelection("prediction")), portIndex = 0)
    def getPredictionColumn = $(predictionColumnParameter)
    def setPredictionColumn(value: SingleColumnSelection): this.type = set(predictionColumnParameter, value)
  }

  case class AreaUnderROC() extends Metric(areaUnderROC) with RawPredictionMetric {
    val name = areaUnderROC
    override val parameterGroups = List(ParameterGroup("", rawPredictionColumnParameter))
  }

  case class AreaUnderPR() extends Metric(areaUnderPR) with RawPredictionMetric {
    val name = areaUnderPR
    override val parameterGroups = List(ParameterGroup("", rawPredictionColumnParameter))
  }

  case class Precision() extends Metric(precision) with PredictionMetric {
    val name = precision
    override val parameterGroups = List(ParameterGroup("", predictionColumnParameter))
  }

  case class Recall() extends Metric(recall) with PredictionMetric {
    val name = recall
    override val parameterGroups = List(ParameterGroup("", predictionColumnParameter))
  }

  case class F1Score() extends Metric(f1Score) with PredictionMetric {
    val name = f1Score
    override val parameterGroups = List(ParameterGroup("", predictionColumnParameter))
  }
}
