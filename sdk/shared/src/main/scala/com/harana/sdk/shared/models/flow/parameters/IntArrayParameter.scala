package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.{ComplexArrayValidator, Validator}


case class IntArrayParameter(name: String,
                             validator: Validator[Array[Int]] = ComplexArrayValidator.allInt)
    extends Parameter[Array[Int]] {

  val parameterType = ParameterType.MultipleNumeric

  override def replicate(name: String): IntArrayParameter = copy(name = name)
  override def validate(values: Array[Int]) = validator.validate(name, values)
}