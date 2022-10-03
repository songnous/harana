package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow.{ExecutionContext, Knowledge}
import com.harana.sdk.backend.models.flow.actionobjects.Evaluator
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actiontypes.exceptions.TooManyPossibleTypesError
import com.harana.sdk.backend.models.flow.inference.InferContext
import com.harana.sdk.shared.models.flow.actionobjects.MetricValue
import com.harana.sdk.shared.models.flow.actiontypes.EvaluateInfo

import scala.reflect.runtime.universe.TypeTag

class Evaluate extends ActionTypeType2To1[Evaluator, DataFrame, MetricValue] with EvaluateInfo {

  def execute(evaluator: Evaluator, dataFrame: DataFrame)(context: ExecutionContext) =
    evaluatorWithParameters(evaluator).evaluate(context)(())(dataFrame)

  override def inferKnowledge(evaluatorKnowledge: Knowledge[Evaluator], dataFrameKnowledge: Knowledge[DataFrame])(context: InferContext) = {
    if (evaluatorKnowledge.size > 1) throw TooManyPossibleTypesError().toException
    val evaluator = evaluatorKnowledge.single
    evaluatorWithParameters(evaluator).evaluate.infer(context)(())(dataFrameKnowledge)
  }

   def evaluatorWithParameters(evaluator: Evaluator): Evaluator = {
    val evaluatorWithParameters = evaluator.replicate().setParametersFromJson(getEvaluatorParameters, ignoreNulls = true)
    validateDynamicParameters(evaluatorWithParameters)
    evaluatorWithParameters
  }

  lazy val tTagTO_0: TypeTag[MetricValue] = typeTag

}