package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.{RegexValidator, Validator}
import io.circe.generic.JsonCodec

@JsonCodec
case class EmailParameter(name: String,
                          required: Boolean = false,
                          default: Option[String] = None,
                          pattern: Option[String] = None,
                          validator: Validator[String] = RegexValidator.Email) extends Parameter[String] with HasValidator[String] {

  val parameterType = ParameterType.Email

  override def replicate(name: String) = copy(name = name)
}