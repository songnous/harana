package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.{AcceptAllRegexValidator, Validator}

case class StringParameter(name: String,
                           description: Option[String],
                           validator: Validator[String] = new AcceptAllRegexValidator) extends Parameter[String] with HasValidator[String] {

  val parameterType = ParameterType.String

  override def replicate(name: String) = copy(name = name)
}