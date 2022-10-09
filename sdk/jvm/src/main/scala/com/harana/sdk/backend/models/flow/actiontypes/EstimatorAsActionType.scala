package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow.{ExecutionContext, Knowledge}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actionobjects.{Estimator, Transformer}
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.shared.models.flow.actionobjects.{EstimatorInfo, TransformerInfo}
import com.harana.sdk.shared.models.flow.actiontypes.EstimatorAsActionInfo
import com.harana.sdk.shared.models.flow.actiontypes.FitInfo.{extractParameterMap, validateDynamicParameters}
import com.harana.sdk.shared.models.flow.utils.TypeUtils
import izumi.reflect.Tag

import scala.reflect.runtime.universe.{TypeTag, typeTag}

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