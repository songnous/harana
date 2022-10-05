package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.{RangeValidator, Validator}
import io.circe.generic.JsonCodec

@JsonCodec
case class NumericParameter(name: String,
                            required: Boolean = false,
                            default: Option[Double] = None,
                            validator: Validator[Double] = RangeValidator.allDouble)
    extends Parameter[Double] with HasValidator[Double] {

  val parameterType = ParameterType.Numeric

  override val isGriddable: Boolean = true

  override def replicate(name: String) = copy(name = name)

}
