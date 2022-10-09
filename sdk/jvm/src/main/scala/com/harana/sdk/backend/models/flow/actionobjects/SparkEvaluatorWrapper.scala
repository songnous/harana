package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.{ExecutionContext, Knowledge}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.shared.models.flow.actionobjects.MetricValue
import com.harana.sdk.shared.models.flow.utils.TypeUtils
import izumi.reflect.Tag
import org.apache.spark.ml

import scala.reflect.runtime.universe._

abstract class SparkEvaluatorWrapper[E <: ml.evaluation.Evaluator](implicit val evaluatorTag: Tag[E])
  extends Evaluator
  with ParametersWithSparkWrappers {

  val sparkEvaluator = createEvaluatorInstance()

  def getMetricName: String

  def _evaluate(context: ExecutionContext, dataFrame: DataFrame) = {
    val sparkParameters = sparkParamMap(sparkEvaluator, dataFrame.sparkDataFrame.schema)
    val value = sparkEvaluator.evaluate(dataFrame.sparkDataFrame, sparkParameters)
    MetricValue(getMetricName, value)
  }

  def _infer(k: Knowledge[DataFrame]) = {
    k.single.schema.foreach(sparkParamMap(sparkEvaluator, _))
    MetricValue.forInference(getMetricName)
  }

  def createEvaluatorInstance() = TypeUtils.instanceOfType(evaluatorTag)

  override def isLargerBetter = sparkEvaluator.isLargerBetter

}