package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.{ArrayValidator, RegexValidator, Validator}
import io.circe.generic.JsonCodec

@JsonCodec
case class StringArrayParameter(name: String,
                                required: Boolean = false,
                                default: Option[Array[String]] = None,
                                validator: Validator[Array[String]] = ArrayValidator(RegexValidator.AcceptAll)) extends Parameter[Array[String]] {

  val parameterType = ParameterType.MultipleString

  override def replicate(name: String): StringArrayParameter = copy(name = name)
  override def validate(values: Array[String]) = validator.validate(name, values)
}

object StringArrayParameter {
  implicit val valueMapping = new Parameter.Values[StringArrayParameter, Array[String]]
}