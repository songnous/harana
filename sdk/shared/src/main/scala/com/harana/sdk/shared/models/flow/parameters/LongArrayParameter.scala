package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.{RangeValidator, Validator}
import io.circe.generic.JsonCodec

@JsonCodec
case class LongArrayParameter(name: String,
                              default: Option[Long] = None,
                              tags: List[String] = List(),
                              required: Boolean = false,
                              placeholder: Option[Long] = None,
                              validator: Validator[Long] = RangeValidator.allLong) extends Parameter[Long] with HasValidator[Long] {

  val parameterType = ParameterType.LongArray

  override val isGriddable: Boolean = true

  override def replicate(name: String) = copy(name = name)

}