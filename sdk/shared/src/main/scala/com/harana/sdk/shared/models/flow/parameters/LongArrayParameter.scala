package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.{ComplexArrayValidator, RangeValidator, Validator}
import io.circe.generic.JsonCodec

@JsonCodec
case class LongArrayParameter(name: String,
                              required: Boolean = false,
                              default: Option[Array[Long]] = None,
                              validator: Validator[Array[Long]] = ComplexArrayValidator.allLong) extends Parameter[Array[Long]] with HasValidator[Array[Long]] {

  val parameterType = ParameterType.LongArray

  override val isGriddable: Boolean = true

  override def replicate(name: String) = copy(name = name)

}