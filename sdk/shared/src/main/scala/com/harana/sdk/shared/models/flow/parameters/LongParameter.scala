package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.{RangeValidator, Validator}
import io.circe.generic.JsonCodec

@JsonCodec
case class LongParameter(name: String,
                         required: Boolean = false,
                         default: Option[Long] = None,
                         maxLength: Option[Int] = None,
                         placeholder: Option[Long] = None,
                         pattern: Option[String] = None,
                         validator: Validator[Long] = RangeValidator.allLong)
  extends Parameter[Long] with HasValidator[Long] {

  val parameterType = ParameterType.Numeric

  override val isGriddable: Boolean = true

  override def replicate(name: String) = copy(name = name)

}