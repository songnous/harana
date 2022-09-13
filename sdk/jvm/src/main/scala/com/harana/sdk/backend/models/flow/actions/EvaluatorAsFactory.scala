package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.flow.actionobjects.Evaluator
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.flow.{Action0To1, ExecutionContext, Knowledge}
import com.harana.sdk.shared.models.flow.utils.TypeUtils

import scala.reflect.runtime.universe.TypeTag

abstract class EvaluatorAsFactory[T <: Evaluator](implicit typeTag: TypeTag[T]) extends Action0To1[T] {

  val evaluator: T = TypeUtils.instanceOfType(typeTag)

  lazy val tTagTO_0: TypeTag[T] = typeTag[T]

  val parameters = evaluator.parameters

  setDefault(evaluator.extractParameterMap().toSeq: _*)

  def execute()(context: ExecutionContext): T = updatedEvaluator

  override def inferKnowledge()(context: InferContext) = (Knowledge[T](updatedEvaluator), InferenceWarnings.empty)

  private def updatedEvaluator: T = evaluator.set(extractParameterMap())

}
