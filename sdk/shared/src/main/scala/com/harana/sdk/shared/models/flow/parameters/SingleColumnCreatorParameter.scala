package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.{ColumnValidator, Validator}
import io.circe.generic.JsonCodec

@JsonCodec
case class SingleColumnCreatorParameter(name: String,
                                        required: Boolean = false,
                                        default: Option[String] = None) extends Parameter[String] with HasValidator[String] {

  val validator: Validator[String] = ColumnValidator.Name

  val parameterType = ParameterType.SingleColumnCreator

  override def replicate(name: String): SingleColumnCreatorParameter = copy(name = name)

}