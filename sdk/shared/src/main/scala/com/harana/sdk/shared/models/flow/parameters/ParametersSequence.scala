package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.exceptions.NoArgumentConstructorRequiredError
import com.harana.sdk.shared.models.flow.utils.TypeUtils

import scala.reflect.runtime.universe._

case class ParametersSequence[T <: Parameters](name: String, description: Option[String])(implicit tag: TypeTag[T]) extends Parameter[Seq[T]] {

  val parameterType = ParameterType.Multiplier

  private val constructor = TypeUtils.constructorForTypeTag(tag).getOrElse {
    throw NoArgumentConstructorRequiredError(tag.tpe.typeSymbol.asClass.name.decodedName.toString).toException
  }

  private def innerParametersInstance: T = constructor.newInstance()

  override def replicate(name: String) = copy(name = name)

  override def validate(value: Seq[T]) = value.flatMap(_.validateParameters).toVector

}
