package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow.{ExecutionContext, Knowledge}
import com.harana.sdk.backend.models.flow.actionobjects.Transformer
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.shared.models.flow.actiontypes.TransformInfo

import scala.reflect.runtime.universe.TypeTag

class Transform extends ActionTypeType2To1[Transformer, DataFrame, DataFrame] with TransformInfo {

  def execute(transformer: Transformer, dataFrame: DataFrame)(context: ExecutionContext) =
    transformerWithParameters(transformer).transform(context)(())(dataFrame)

  override def inferKnowledge(transformerKnowledge: Knowledge[Transformer], dataFrameKnowledge: Knowledge[DataFrame])(context: InferContext) =
    if (transformerKnowledge.size > 1)
      (Knowledge(DataFrame.forInference()), InferenceWarnings.empty)
    else {
      val transformer = transformerKnowledge.single
      transformerWithParameters(transformer).transform.infer(context)(())(dataFrameKnowledge)
    }

  def transformerWithParameters(transformer: Transformer) = {
    val transformerWithParameters = transformer.replicate().setParametersFromJson(getTransformerParameters, ignoreNulls = true)
    validateDynamicParameters(transformerWithParameters)
    transformerWithParameters
  }

  lazy val tTagTO_0: TypeTag[DataFrame] = typeTag

}