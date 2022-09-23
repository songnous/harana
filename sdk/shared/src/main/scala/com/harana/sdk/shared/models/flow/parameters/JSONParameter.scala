package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.{RegexValidator, Validator}
import io.circe.generic.JsonCodec

@JsonCodec
case class JSONParameter(name: String,
                         tags: List[String] = List(),
                         required: Boolean = false,
                         placeholder: Option[String] = None,
                         options: List[(String, String)] = List(),
                         validator: Validator[String] = RegexValidator.AcceptAll) extends Parameter[String] with HasValidator[String] {

  val parameterType = ParameterType.JSON

  override def replicate(name: String) = copy(name = name)
}