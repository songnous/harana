package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.{ColumnNameValidator, Validator}

case class SingleColumnCreatorParameter(name: String,
                                        description: Option[String]) extends Parameter[String] with HasValidator[String] {

  val validator: Validator[String] = ColumnNameValidator

  val parameterType = ParameterType.SingleColumnCreator

  override def replicate(name: String): SingleColumnCreatorParameter = copy(name = name)

}