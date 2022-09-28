package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.{RegexValidator, Validator}
import io.circe.generic.JsonCodec

@JsonCodec
case class IPAddressParameter(name: String,
                              required: Boolean = false,
                              default: Option[(String, Long)] = None,
                              portDefault: Option[String] = None,
                              options: List[(String, String)] = List(),
                              validator: Validator[(String, Long)] = null)
  extends Parameter[(String, Long)] with HasValidator[(String, Long)] {

  val parameterType = ParameterType.String

  override def replicate(name: String) = copy(name = name)
}