package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.evaluators

import com.harana.sdk.backend.models.flow.{ExecutionContext, Knowledge}
import com.harana.sdk.backend.models.flow.actionobjects.Evaluator
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.{DataFrame, DataFrameColumnsGetter}
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.evaluators.BinaryClassificationEvaluatorInfo.{AreaUnderPR, AreaUnderROC, F1Score, PredictionMetric, RawPredictionMetric}
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.evaluators.MulticlassClassificationEvaluatorInfo.{Precision, Recall}
import com.harana.sdk.shared.models.flow.actionobjects.MetricValue
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.evaluators.BinaryClassificationEvaluatorInfo
import com.harana.sdk.shared.models.flow.utils.ColumnType
import com.harana.spark.Linalg.Vector
import org.apache.spark.mllib.evaluation.{BinaryClassificationMetrics, MulticlassMetrics}
import org.apache.spark.sql.Row

class BinaryClassificationEvaluator extends Evaluator with BinaryClassificationEvaluatorInfo {

  def _evaluate(context: ExecutionContext, dataFrame: DataFrame) = {
    val labelColumnName = dataFrame.getColumnName(getLabelColumn)
    val metric = getMetricName match {
      case rawPredictionChoice: RawPredictionMetric => evaluateRawPrediction(dataFrame, labelColumnName, rawPredictionChoice)
      case predChoice: PredictionMetric => evaluatePrediction(dataFrame, labelColumnName, predChoice)
    }
    MetricValue(getMetricName.name, metric)
  }

  private def evaluateRawPrediction(dataFrame: DataFrame, labelColumnName: String, rawChoice: RawPredictionMetric) = {
    val rawPredictionColumnName = dataFrame.getColumnName(rawChoice.getRawPredictionColumn)
    val scoreAndLabels = dataFrame.sparkDataFrame.select(rawPredictionColumnName, labelColumnName).rdd.map {
      case Row(rawPrediction: Vector, label: Double) => (rawPrediction(1), label)
    }
    val metrics = new BinaryClassificationMetrics(scoreAndLabels)
    val metric = rawChoice match {
      case _: AreaUnderROC => metrics.areaUnderROC()
      case _: AreaUnderPR => metrics.areaUnderPR()
    }
    metrics.unpersist()
    metric
  }

  private def evaluatePrediction(dataFrame: DataFrame, labelColumnName: String, predChoice: PredictionMetric) = {
    val predictionColumnName = dataFrame.getColumnName(predChoice.getPredictionColumn)
    val predictionAndLabels  = dataFrame.sparkDataFrame.select(predictionColumnName, labelColumnName).rdd.map {
      case Row(prediction: Double, label: Double) => (prediction, label)
    }
    val metrics = new MulticlassMetrics(predictionAndLabels)
    val metric = predChoice match {
      case _: Precision => metrics.precision(1.0)
      case _: Recall => metrics.recall(1.0)
      case _: F1Score => metrics.fMeasure(1.0)
    }
    metric
  }

  def _infer(k: Knowledge[DataFrame]) = {
    k.single.schema.foreach { schema =>
      DataFrameColumnsGetter.assertExpectedColumnType(schema, $(labelColumnParameter), ColumnType.Numeric)
      getMetricName match {
        case rawChoice: RawPredictionMetric => DataFrameColumnsGetter.assertExpectedColumnType(schema, rawChoice.getRawPredictionColumn, ColumnType.Vector)
        case predChoice: PredictionMetric => DataFrameColumnsGetter.assertExpectedColumnType(schema, predChoice.getPredictionColumn, ColumnType.Numeric)
      }
    }
    MetricValue.forInference(getMetricName.name)
  }

  override def isLargerBetter: Boolean = true
}
