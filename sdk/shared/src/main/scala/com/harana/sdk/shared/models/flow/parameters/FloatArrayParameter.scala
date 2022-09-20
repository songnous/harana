package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.{RangeValidator, Validator}

case class FloatArrayParameter(name: String,
                               tags: List[String] = List(),
                               required: Boolean = false,
                               validator: Validator[Float] = RangeValidator.allFloat)
  extends Parameter[Float] with HasValidator[Float] {

  val parameterType = ParameterType.MultipleNumeric

  override val isGriddable: Boolean = true

  override def replicate(name: String) = copy(name = name)

}