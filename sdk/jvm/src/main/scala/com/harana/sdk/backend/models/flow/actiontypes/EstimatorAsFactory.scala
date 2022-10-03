package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow.actionobjects.{Estimator, Transformer}
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.flow.{ExecutionContext, Knowledge}
import com.harana.sdk.shared.models.flow.utils.TypeUtils

import scala.reflect.runtime.universe.TypeTag

abstract class EstimatorAsFactory[E <: Estimator[Transformer]](implicit typeTagE: TypeTag[E]) extends ActionTypeType0To1[E] {

  lazy val tTagTO_0: TypeTag[E] = typeTag[E]

   val estimator: E = TypeUtils.instanceOfType(typeTagE)

  def execute()(context: ExecutionContext): E = updatedEstimator

  override def inferKnowledge()(context: InferContext) = (Knowledge[E](updatedEstimator), InferenceWarnings.empty)

  private def updatedEstimator: E = estimator.set(extractParameterMap())

}