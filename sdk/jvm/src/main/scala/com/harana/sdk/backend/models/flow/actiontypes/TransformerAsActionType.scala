package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow.actionobjects.Transformer
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.flow.utils.TypeUtils
import com.harana.sdk.backend.models.flow.{ExecutionContext, Knowledge}
import izumi.reflect.Tag

abstract class TransformerAsActionType[T <: Transformer]()(implicit tag: Tag[T]) extends ActionTypeType1To2[DataFrame, DataFrame, T] {

  lazy val transformer: T = TypeUtils.instanceOfType(tag)

  def execute(t0: DataFrame)(context: ExecutionContext): (DataFrame, T) = {
    transformer.set(extractParameterMap())
    (transformer.transform(context)(())(t0), transformer)
  }

  override def inferKnowledge(dfKnowledge: Knowledge[DataFrame])(ctx: InferContext): ((Knowledge[DataFrame], Knowledge[T]), InferenceWarnings) = {
    transformer.set(extractParameterMap())
    val (outputDfKnowledge, warnings) = transformer.transform.infer(ctx)(())(dfKnowledge)
    ((outputDfKnowledge, Knowledge(transformer)), warnings)
  }

  lazy val tTagTO_0: Tag[DataFrame] = typeTag
  lazy val tTagTO_1 = tag

}