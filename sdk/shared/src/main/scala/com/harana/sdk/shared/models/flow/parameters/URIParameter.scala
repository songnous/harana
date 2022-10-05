package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.{RegexValidator, Validator}
import io.circe.generic.JsonCodec

@JsonCodec
case class URIParameter(name: String,
                        required: Boolean = false,
                        default: Option[String] = None,
                        options: List[(String, String)] = List(),
                        pattern: Option[String] = None,
                        validator: Validator[String] = RegexValidator.URI) extends Parameter[String] with HasValidator[String] {

  val parameterType = ParameterType.URI

  override def replicate(name: String) = copy(name = name)
}