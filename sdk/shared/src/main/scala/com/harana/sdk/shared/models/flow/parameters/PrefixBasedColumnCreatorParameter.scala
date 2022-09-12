package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.ColumnPrefixNameValidator

case class PrefixBasedColumnCreatorParameter(name: String,
                                             description: Option[String]) extends Parameter[String] {

  override def validate(value: String) = ColumnPrefixNameValidator.validate(name, value) ++ super.validate(value)

  val parameterType = ParameterType.PrefixBasedColumnCreator

  override def replicate(name: String): PrefixBasedColumnCreatorParameter = copy(name = name)

}

trait EmptyPrefixValidator extends PrefixBasedColumnCreatorParameter {

  override def validate(value: String) =
    if (value.isEmpty) Vector() else super.validate(value)

}