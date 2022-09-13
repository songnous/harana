package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.flow.{Action1To2, ExecutionContext, Knowledge}
import com.harana.sdk.backend.models.flow.actionobjects.Transformer
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.shared.models.flow.utils.TypeUtils

import scala.reflect.runtime.universe.{TypeTag, typeTag}

abstract class TransformerAsAction[T <: Transformer]()(implicit tag: TypeTag[T]) extends Action1To2[DataFrame, DataFrame, T] {

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

  lazy val tTagTO_0: TypeTag[DataFrame] = typeTag
  lazy val tTagTO_1 = tag
}