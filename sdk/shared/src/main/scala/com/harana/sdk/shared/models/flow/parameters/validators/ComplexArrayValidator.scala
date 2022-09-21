
package com.harana.sdk.shared.models.flow.parameters.validators

import com.harana.sdk.shared.models.flow.exceptions.FlowError

case class ComplexArrayValidator[T <: Any](rangeValidator: RangeValidator[T], lengthValidator: ArrayLengthValidator = ArrayLengthValidator.withAtLeast(1)) extends Validator[Array[T]] {
  val validatorType = ValidatorType.ArrayComplex

  def validate(name: String, parameter: Array[T]): List[FlowError] = {
    val arrayExceptions = lengthValidator.validate(name, parameter)
    val elementsRangesExceptions = parameter.zipWithIndex.flatMap { case (value, idx) =>
      rangeValidator.validate(s"$name[$idx]", value)
    }.toList
    arrayExceptions ++ elementsRangesExceptions
  }

  override def toHumanReadable(parameterName: String) =
    lengthValidator.toHumanReadable(parameterName) ++ " " ++ rangeValidator.toHumanReadable(s"$parameterName[i]")
}

object ComplexArrayValidator {
  def allDouble = ComplexArrayValidator(rangeValidator = RangeValidator.allDouble, lengthValidator = ArrayLengthValidator.all)
  def allFloat = ComplexArrayValidator(rangeValidator = RangeValidator.allFloat, lengthValidator = ArrayLengthValidator.all)
  def allInt = ComplexArrayValidator(rangeValidator = RangeValidator.allInt, lengthValidator = ArrayLengthValidator.all)
  def allLong = ComplexArrayValidator(rangeValidator = RangeValidator.allLong, lengthValidator = ArrayLengthValidator.all)
}