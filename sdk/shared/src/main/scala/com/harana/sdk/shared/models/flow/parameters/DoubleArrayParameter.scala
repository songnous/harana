package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.{ComplexArrayValidator, Validator}

case class DoubleArrayParameter(name: String,
                                required: Boolean = false,
                                default: Option[Double] = None,
                                validator: Validator[Array[Double]] = ComplexArrayValidator.allDouble)
  extends Parameter[Array[Double]] {

  val parameterType = ParameterType.MultipleNumeric

  override def replicate(name: String): DoubleArrayParameter = copy(name = name)
  override def validate(values: Array[Double]) = validator.validate(name, values)
}