package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.{ComplexArrayValidator, Validator}
import io.circe.generic.JsonCodec

@JsonCodec
case class IntArrayParameter(name: String,
                             required: Boolean = false,
                             default: Option[Array[Int]] = None,
                             validator: Validator[Array[Int]] = ComplexArrayValidator.allInt)
    extends Parameter[Array[Int]] {

  val parameterType = ParameterType.MultipleNumeric

  override def replicate(name: String): IntArrayParameter = copy(name = name)
  override def validate(values: Array[Int]) = validator.validate(name, values)
}