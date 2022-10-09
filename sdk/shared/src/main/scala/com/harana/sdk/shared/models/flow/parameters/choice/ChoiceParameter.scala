package com.harana.sdk.shared.models.flow.parameters.choice

import com.harana.sdk.shared.models.flow.parameters.ParameterType
import izumi.reflect.Tag

import scala.reflect.runtime.universe._

class ChoiceParameter[T <: Choice](val name: String,
                                   val required: Boolean = false,
                                   val default: Option[T] = None)(implicit tag: Tag[T]) extends AbstractChoiceParameter[T, T] {

  val parameterType = ParameterType.Choice

  override def validate(value: T) = value.validateParameters

  override def replicate(name: String) = new ChoiceParameter[T](name)
}

object ChoiceParameter {
  def apply[T <: Choice: Tag](name: String,
                                  required: Boolean = false,
                                  default: Option[T] = None) = new ChoiceParameter[T](name, required, default)
}
