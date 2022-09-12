package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.{RangeValidator, Validator}

case class LongParameter(name: String, description: Option[String], validator: Validator[Long] = RangeValidator.allLong)
  extends Parameter[Long] with HasValidator[Long] {

  val parameterType = ParameterType.Numeric

  override val isGriddable: Boolean = true

  override def replicate(name: String) = copy(name = name)

}