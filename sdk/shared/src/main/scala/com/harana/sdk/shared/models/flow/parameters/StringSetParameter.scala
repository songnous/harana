package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.validators.{RegexValidator, SetValidator, Validator}

case class StringSetParameter(name: String,
                              required: Boolean = false,
                              default: Option[Set[String]] = None,
                              validator: Validator[Set[String]] = SetValidator(RegexValidator.AcceptAll)) extends Parameter[Set[String]] {

  val parameterType = ParameterType.MultipleString

  override def replicate(name: String): StringSetParameter = copy(name = name)
  override def validate(values: Set[String]) = validator.validate(name, values)
}

object StringSetParameter {
  implicit val valueMapping = new Parameter.Values[StringSetParameter, Set[String]]
}