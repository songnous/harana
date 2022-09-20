package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.{RegexValidator, Validator}

case class IPAddressListParameter(name: String,
                                  default: Option[String] = None,
                                  portDefault: Option[Int] = None,
                                  tags: List[String] = List(),
                                  required: Boolean = false,
                                  port: Boolean = false,
                                  placeholder: Option[String] = None,
                                  validator: Validator[String] = RegexValidator.AcceptAll) extends Parameter[String] with HasValidator[String] {

  val parameterType = ParameterType.IPAddress

  override def replicate(name: String) = copy(name = name)
}