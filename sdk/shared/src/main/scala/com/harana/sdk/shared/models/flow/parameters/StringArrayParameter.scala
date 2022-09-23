package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.{ArrayValidator, RegexValidator, Validator}

case class StringArrayParameter(name: String,
                                required: Boolean = false,
                                default: Option[Array[String]] = None,
                                validator: Validator[Array[String]] = ArrayValidator(RegexValidator.AcceptAll)) extends Parameter[Array[String]] {

  val parameterType = ParameterType.MultipleNumeric

  override def replicate(name: String): StringArrayParameter = copy(name = name)
  override def validate(values: Array[String]) = validator.validate(name, values)
}