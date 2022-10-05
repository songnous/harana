package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.{ComplexArrayValidator, Validator}
import io.circe.generic.JsonCodec

@JsonCodec
case class MultipleNumericParameter(name: String,
                                    required: Boolean = false,
                                    default: Option[Array[Double]] = None,
                                    validator: Validator[Array[Double]] = ComplexArrayValidator.allDouble)
    extends Parameter[Array[Double]] {

  val parameterType = ParameterType.MultipleNumeric

  override def replicate(name: String): MultipleNumericParameter = copy(name = name)
  override def validate(values: Array[Double]) = validator.validate(name, values)
}