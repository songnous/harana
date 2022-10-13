package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow.actionobjects.Evaluator
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.flow.utils.TypeUtils
import com.harana.sdk.backend.models.flow.{ExecutionContext, Knowledge}
import izumi.reflect.Tag

abstract class EvaluatorAsFactory[T <: Evaluator](implicit typeTag: Tag[T]) extends ActionTypeType0To1[T] {

  val evaluator: T = TypeUtils.instanceOfType(typeTag)

  lazy val tTagTO_0: Tag[T] = typeTag[T]

  override val parameterGroups = evaluator.parameterGroups

  setDefault(evaluator.extractParameterMap().toSeq: _*)

  def execute()(context: ExecutionContext): T = updatedEvaluator

  override def inferKnowledge()(context: InferContext) = (Knowledge[T](updatedEvaluator), InferenceWarnings.empty)

  private def updatedEvaluator: T = evaluator.set(extractParameterMap())

}
