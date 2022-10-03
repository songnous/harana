package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow.{ExecutionContext, Knowledge}
import com.harana.sdk.backend.models.flow.actionobjects.Transformer
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.shared.models.flow.actiontypes.FitInfo.extractParameterMap
import com.harana.sdk.shared.models.flow.utils.TypeUtils

import scala.reflect.runtime.universe.TypeTag

abstract class TransformerAsFactory[T <: Transformer](implicit typeTag: TypeTag[T]) extends ActionTypeType0To1[T] {

  val transformer: T = TypeUtils.instanceOfType(typeTag)

  def execute()(context: ExecutionContext): T = updatedTransformer

  override def inferKnowledge()(context: InferContext) = (Knowledge[T](updatedTransformer), InferenceWarnings.empty)

  private def updatedTransformer: T = transformer.set(extractParameterMap())

}
