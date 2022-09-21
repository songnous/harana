package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.flow.{ActionType2To1, ExecutionContext, Knowledge}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actionobjects.{Estimator, Transformer}
import com.harana.sdk.backend.models.flow.actions.exceptions.TooManyPossibleTypesError
import com.harana.sdk.backend.models.flow.inference.InferContext
import com.harana.sdk.shared.models.flow.actions.FitInfo

import scala.reflect.runtime.universe.TypeTag

class Fit extends ActionType2To1[Estimator[Transformer], DataFrame, Transformer]
  with FitInfo {

  def execute(estimator: Estimator[Transformer], dataFrame: DataFrame)(ctx: ExecutionContext) =
    estimatorWithParameters(estimator).fit(ctx)(())(dataFrame)

  override def inferKnowledge(estimatorKnowledge: Knowledge[Estimator[Transformer]], dataFrameKnowledge: Knowledge[DataFrame])(ctx: InferContext) = {
    if (estimatorKnowledge.size > 1) throw TooManyPossibleTypesError().toException
    val estimator = estimatorKnowledge.single
    estimatorWithParameters(estimator).fit.infer(ctx)(())(dataFrameKnowledge)
  }

  private def estimatorWithParameters(estimator: Estimator[Transformer]) = {
    val estimatorWithParameters = estimator.replicate().setParametersFromJson($(estimatorParameters), ignoreNulls = true)
    validateDynamicParameters(estimatorWithParameters)
    estimatorWithParameters
  }

  lazy val tTagTO_0: TypeTag[Transformer] = typeTag

}