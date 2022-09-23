package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.ColumnValidator

case class PrefixBasedColumnCreatorParameter(name: String,
                                             required: Boolean = false,
                                             emptyPrefixValidation: Boolean = false) extends Parameter[String] {

  override def validate(value: String) =
    if (emptyPrefixValidation && value.isEmpty)
      List.empty
    else
        ColumnValidator.Name.validate(name, value) ++ super.validate(value)

  val parameterType = ParameterType.PrefixBasedColumnCreator

  override def replicate(name: String): PrefixBasedColumnCreatorParameter = copy(name = name)

}