package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow.actionobjects.Transformer
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.flow.utils.TypeUtils
import com.harana.sdk.backend.models.flow.{ExecutionContext, Knowledge}
import izumi.reflect.Tag

abstract class TransformerAsFactory[T <: Transformer](implicit typeTag: Tag[T]) extends ActionTypeType0To1[T] {

  val transformer: T = TypeUtils.instanceOfType(typeTag)

  def execute()(context: ExecutionContext): T = updatedTransformer

  override def inferKnowledge()(context: InferContext) = (Knowledge[T](updatedTransformer), InferenceWarnings.empty)

  private def updatedTransformer: T = transformer.set(extractParameterMap())

}
