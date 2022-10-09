package com.harana.sdk.shared.models.flow.parameters

import izumi.reflect.Tag

case class ParametersSequence[T <: Parameters](name: String,
                                               required: Boolean = false,
                                               default: Option[Seq[T]] = None)(implicit tag: Tag[T]) extends Parameter[Seq[T]] {

  val parameterType = ParameterType.Multiplier

  // FIXME
//  private def innerParametersInstance: T = TypeUtils.constructorForTypeTag(tag).getOrElse {
//    throw NoArgumentConstructorRequiredError(tag.closestClass.getName).toException
//  }.newInstance()

  override def replicate(name: String) = copy(name = name)

  override def validate(value: Seq[T]) = value.flatMap(_.validateParameters).toList

}
