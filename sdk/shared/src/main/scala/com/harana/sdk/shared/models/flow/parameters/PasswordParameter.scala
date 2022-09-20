package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.{RegexValidator, Validator}

case class PasswordParameter(name: String,
                             tags: List[String] = List(),
                             required: Boolean = false,
                             placeholder: Option[String] = None,
                             validator: Validator[String] = RegexValidator.AcceptAll) extends Parameter[String] with HasValidator[String] {

  val parameterType = ParameterType.Password

  override def replicate(name: String) = copy(name = name)
}