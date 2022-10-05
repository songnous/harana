package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.{RangeValidator, Validator}
import io.circe.generic.JsonCodec

@JsonCodec
case class FloatParameter(name: String,
                          required: Boolean = false,
                          default: Option[Float] = None,
                          validator: Validator[Float] = RangeValidator.allFloat)
  extends Parameter[Float] with HasValidator[Float] {

  val parameterType = ParameterType.Numeric

  override val isGriddable: Boolean = true

  override def replicate(name: String) = copy(name = name)

}