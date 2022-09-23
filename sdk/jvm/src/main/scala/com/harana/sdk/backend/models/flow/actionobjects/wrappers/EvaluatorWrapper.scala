package com.harana.sdk.backend.models.flow.actionobjects.wrappers

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.Evaluator
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.parameters.wrappers.ParamWrapper
import com.harana.spark.ML
import org.apache.spark.ml.evaluation
import org.apache.spark.ml.param.{Param, ParamMap}
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.sql

class EvaluatorWrapper(context: ExecutionContext, evaluator: Evaluator) extends ML.Evaluator {

  def evaluateDF(dataset: sql.DataFrame): Double =
    evaluator.evaluate(context)(())(DataFrame.fromSparkDataFrame(dataset.toDF())).value

  def copy(extra: ParamMap): evaluation.Evaluator = {
    val parameters = ParamTransformer.transform(extra)
    val evaluatorCopy = evaluator.replicate().set(parameters: _*)
    new EvaluatorWrapper(context, evaluatorCopy)
  }

  override lazy val params: Array[Param[_]] =
    evaluator.allParameters.map(new ParamWrapper(uid, _))

  override def isLargerBetter: Boolean = evaluator.isLargerBetter

  val uid = Identifiable.randomUID("EvaluatorWrapper")

}
