package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actionobjects.{Estimator, Transformer}
import com.harana.sdk.backend.models.flow.inference.InferContext
import com.harana.sdk.backend.models.flow.utils.TypeUtils
import com.harana.sdk.backend.models.flow.{ExecutionContext, Knowledge}
import izumi.reflect.Tag

abstract class EstimatorAsActionType[E <: Estimator[T], T <: Transformer]()(implicit typeTagE: Tag[E], typeTagT: Tag[T])
  extends ActionTypeType1To2[DataFrame, DataFrame, T] {

  val estimator: E = TypeUtils.instanceOfType(typeTagE)

  override val parameterGroups = estimator.parameterGroups

  setDefault(estimator.extractParameterMap().toSeq: _*)

  def execute(t0: DataFrame)(context: ExecutionContext): (DataFrame, T) = {
    val transformer = estimatorWithParameters.fit(context)(())(t0)
    val transformedDataFrame = transformer.transform(context)(())(t0)
    (transformedDataFrame, transformer)
  }

  override def inferKnowledge(k0: Knowledge[DataFrame])(context: InferContext) = {
    val (transformerKnowledge, fitWarnings) = estimatorWithParameters.fit.infer(context)(())(k0)
    val (dataFrameKnowledge, transformWarnings) = transformerKnowledge.single.transform.infer(context)(())(k0)
    val warnings = fitWarnings ++ transformWarnings
    ((dataFrameKnowledge, transformerKnowledge), warnings)
  }

  private def estimatorWithParameters = {
    val estimatorWithParameters = estimator.set(extractParameterMap())
    validateDynamicParameters(estimatorWithParameters)
    estimatorWithParameters
  }

  lazy val tTagTO_0: Tag[DataFrame] = typeTag[DataFrame]
  lazy val tTagTO_1: Tag[T] = typeTag[T]

}