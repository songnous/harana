package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.{RangeValidator, Validator}
import io.circe.generic.JsonCodec

@JsonCodec
case class LongArrayParameter(name: String,
                              required: Boolean = false,
                              validator: Validator[Long] = RangeValidator.allLong) extends Parameter[Long] with HasValidator[Long] {

  val parameterType = ParameterType.LongArray

  override val isGriddable: Boolean = true

  override def replicate(name: String) = copy(name = name)

}