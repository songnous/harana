package com.harana.sdk.shared.models.flow.parameters.choice

import com.harana.sdk.shared.models.flow.parameters.ParameterType
import com.harana.sdk.shared.models.flow.parameters.ParameterType.MultipleChoice

import scala.reflect.runtime.universe._

case class MultipleChoiceParameter[T <: Choice](name: String)(implicit tag: TypeTag[T]) extends AbstractChoiceParameter[T, Set[T]] {

  val parameterType = MultipleChoice

  override def validate(value: Set[T]) = value.toVector.flatMap(_.validateParameters)

  override def replicate(name: String) = copy(name = name)
}