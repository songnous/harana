package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.{RangeValidator, Validator}
import io.circe.generic.JsonCodec

@JsonCodec
case class IntParameter(name: String,
                        required: Boolean = false,
                        default: Option[Int] = None,
                        options: List[(String, Int)] = List(),
                        maxLength: Option[Int] = None,
                        placeholder: Option[Int] = None,
                        thousandSeparator: Option[String] = None,
                        allowNegative: Boolean = true,
                        allowPositive: Boolean = true,
                        pattern: Option[String] = None,
                        validator: Validator[Int] = RangeValidator.allInt)
  extends Parameter[Int] with HasValidator[Int] {

  val parameterType = ParameterType.Numeric

  override val isGriddable: Boolean = true

  override def replicate(name: String) = copy(name = name)

}