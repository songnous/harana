package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.{ComplexArrayValidator, Validator}
import io.circe.generic.JsonCodec

@JsonCodec
case class FloatArrayParameter(name: String,
                               required: Boolean = false,
                               default: Option[Array[Float]] = None,
                               validator: Validator[Array[Float]] = ComplexArrayValidator.allFloat)
  extends Parameter[Array[Float]] with HasValidator[Array[Float]] {

  val parameterType = ParameterType.MultipleNumeric

  override val isGriddable: Boolean = true

  override def replicate(name: String) = copy(name = name)

}