package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow.{ExecutionContext, Knowledge}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actionobjects.{Estimator, Transformer}
import com.harana.sdk.backend.models.flow.actiontypes.exceptions.TooManyPossibleTypesError
import com.harana.sdk.backend.models.flow.inference.InferContext
import com.harana.sdk.shared.models.flow.actiontypes.FitInfo

import izumi.reflect.Tag

class Fit extends ActionTypeType2To1[Estimator[Transformer], DataFrame, Transformer]
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

  lazy val tTagTO_0: Tag[Transformer] = typeTag

}