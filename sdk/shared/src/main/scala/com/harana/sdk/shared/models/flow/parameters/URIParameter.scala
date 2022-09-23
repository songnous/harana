package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.{RegexValidator, Validator}

case class URIParameter(name: String,
                        required: Boolean = false,
                        options: List[(String, String)] = List(),
                        validator: Validator[String] = RegexValidator.URI) extends Parameter[String] with HasValidator[String] {

  val parameterType = ParameterType.URI

  override def replicate(name: String) = copy(name = name)
}