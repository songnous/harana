package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.{RegexValidator, Validator}
import io.circe.generic.JsonCodec

@JsonCodec
case class IPAddressParameter(name: String,
                              required: Boolean = false,
                              default: Option[(String, String)] = None,
                              portDefault: Option[String] = None,
                              options: List[(String, String)] = List(),
                              validator: Validator[(String, String)] = null)
  extends Parameter[(String, String)] with HasValidator[(String, String)] {

  val parameterType = ParameterType.String

  override def replicate(name: String) = copy(name = name)
}