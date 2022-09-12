package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.flow.{Action2To2, ExecutionContext, Knowledge}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actionobjects.{Estimator, Transformer}
import com.harana.sdk.backend.models.flow.actions.exceptions.TooManyPossibleTypesError
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.shared.models.flow.actions.FitPlusTransformInfo

import scala.reflect.runtime.universe.TypeTag

class FitPlusTransform extends Action2To2[Estimator[Transformer], DataFrame, DataFrame, Transformer] with FitPlusTransformInfo {

  def execute(estimator: Estimator[Transformer], dataFrame: DataFrame)(context: ExecutionContext): (DataFrame, Transformer) = {
    val estimatorToRun = estimatorWithParameters(estimator)
    val transformer = estimatorToRun.fit(context)(())(dataFrame)
    val transformed: DataFrame = transformer.transform(context)(())(dataFrame)
    (transformed, transformer)
  }

  override def inferKnowledge(estimatorKnowledge: Knowledge[Estimator[Transformer]],
                                        inputDataFrameKnowledge: Knowledge[DataFrame])(context: InferContext)
  : ((Knowledge[DataFrame], Knowledge[Transformer]), InferenceWarnings) = {

    val (transformerKnowledge, transformerWarnings) =
      inferTransformer(context, estimatorKnowledge, inputDataFrameKnowledge)

    val (transformedDataFrameKnowledge, transformedDataFrameWarnings) =
      inferDataFrame(context, inputDataFrameKnowledge, transformerKnowledge)

    val warningsSum: InferenceWarnings = transformerWarnings ++ transformedDataFrameWarnings
    ((transformedDataFrameKnowledge, transformerKnowledge), warningsSum)
  }

  private def estimatorWithParameters(estimator: Estimator[Transformer]): Estimator[Transformer] = {
    val estimatorWithParams = estimator.replicate().setParametersFromJson($(estimatorParameters), ignoreNulls = true)
    validateDynamicParameters(estimatorWithParams)
    estimatorWithParams
  }

  private def inferDataFrame(context: InferContext,
                             inputDataFrameKnowledge: Knowledge[DataFrame],
                             transformerKnowledge: Knowledge[Transformer]): (Knowledge[DataFrame], InferenceWarnings) = {
    val (transformedDataFrameKnowledge, transformedDataFrameWarnings) = transformerKnowledge.single.transform.infer(context)(())(inputDataFrameKnowledge)
    (transformedDataFrameKnowledge, transformedDataFrameWarnings)
  }

  private def inferTransformer(context: InferContext,
                               estimatorKnowledge: Knowledge[Estimator[Transformer]],
                               inputDataFrameKnowledge: Knowledge[DataFrame]): (Knowledge[Transformer], InferenceWarnings) = {
    throwIfToManyTypes(estimatorKnowledge)
    val estimator = estimatorWithParameters(estimatorKnowledge.single)
    val (transformerKnowledge, transformerWarnings) = estimator.fit.infer(context)(())(inputDataFrameKnowledge)
    (transformerKnowledge, transformerWarnings)
  }

  private def throwIfToManyTypes(estimatorKnowledge: Knowledge[_]): Unit =
    if (estimatorKnowledge.size > 1) {
      throw TooManyPossibleTypesError().toException
    }

  lazy val tTagTO_0: TypeTag[DataFrame] = typeTag
  lazy val tTagTO_1: TypeTag[Transformer] = typeTag

}