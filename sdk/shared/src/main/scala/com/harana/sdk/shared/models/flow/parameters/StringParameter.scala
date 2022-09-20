package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.{RegexValidator, Validator}

case class StringParameter(name: String, validator: Validator[String] = RegexValidator.AcceptAll) extends Parameter[String] with HasValidator[String] {

  val parameterType = ParameterType.String

  override def replicate(name: String) = copy(name = name)
}