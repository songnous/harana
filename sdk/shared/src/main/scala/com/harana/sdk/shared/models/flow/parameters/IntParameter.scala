package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.{RangeValidator, Validator}

case class IntParameter(name: String, validator: Validator[Int] = RangeValidator.allInt)
  extends Parameter[Int] with HasValidator[Int] {

  val parameterType = ParameterType.Numeric

  override val isGriddable: Boolean = true

  override def replicate(name: String) = copy(name = name)

}