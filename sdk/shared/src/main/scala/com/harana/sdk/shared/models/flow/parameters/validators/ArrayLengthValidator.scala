package com.harana.sdk.shared.models.flow.parameters.validators

import com.harana.sdk.shared.models.flow.parameters.exceptions.{ArrayTooLong, ArrayTooShort}

case class ArrayLengthValidator(min: Int = 1, max: Int = Int.MaxValue) extends Validator[Array[_]] {

  require(min >= 0)
  require(max >= min)

  val validatorType = ValidatorType.ArrayLength

  def validate(name: String, parameter: Array[_]) = {
    val length = parameter.length
    if (length < min) Vector(ArrayTooShort(name, length, min))
    else if (length > max) Vector(ArrayTooLong(name, length, max))
    else Vector.empty
  }

  override def toHumanReadable(parameterName: String) =
    if (min > 0 && max == Int.MaxValue) s"Minimum length of `$parameterName` is $min."
    else if (min == 0 && max < Int.MaxValue) s"Maximum length of `$parameterName` is $max."
    else if (min > 0 && max < Int.MaxValue) s"Length of `$parameterName` must be in range [$min, $max]."
    else s"Array `$parameterName` can be of any length."
}

object ArrayLengthValidator {
  def all = ArrayLengthValidator(min = 0)
  def withAtLeast(n: Int) = ArrayLengthValidator(min = n)
}
