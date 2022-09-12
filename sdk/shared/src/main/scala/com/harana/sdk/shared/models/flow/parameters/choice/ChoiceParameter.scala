package com.harana.sdk.shared.models.flow.parameters.choice

import com.harana.sdk.shared.models.flow.parameters.ParameterType

import scala.reflect.runtime.universe._

class ChoiceParameter[T <: Choice](val name: String, val description: Option[String])(implicit tag: TypeTag[T]) extends AbstractChoiceParameter[T, T] {

  val parameterType = ParameterType.Choice

  override def validate(value: T) = value.validateParameters

  override def replicate(name: String) = new ChoiceParameter[T](name, description)
}

object ChoiceParameter {
  def apply[T <: Choice: TypeTag](name: String, description: Option[String]) = new ChoiceParameter[T](name, description)
}
