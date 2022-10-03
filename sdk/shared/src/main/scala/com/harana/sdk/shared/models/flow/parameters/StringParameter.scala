package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.{RegexValidator, Validator}
import io.circe.generic.JsonCodec

@JsonCodec
case class StringParameter(name: String,
                           required: Boolean = false,
                           default: Option[String] = None,
                           options: List[(String, String)] = List(),
                           placeholder: Option[String] = None,
                           maxLength: Option[Int] = None,
                           multiLine: Boolean = false,
                           inputFormat: Option[String] = None,
                           pattern: Option[String] = None,
                           validator: Validator[String] = RegexValidator.AcceptAll) extends Parameter[String] with HasValidator[String] {

  val parameterType = ParameterType.String

  override def replicate(name: String) = copy(name = name)
}

object StringParameter {
  implicit val valueMapping = new Parameter.Values[StringParameter, String]
}