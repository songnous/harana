package com.harana.sdk.shared.models.flow.parameters.exceptions

case class OutOfRangeError[T](name: String, value: T, lowerBound: T, upperBound: T)(implicit n: Numeric[T]) extends ValidationError {
  val message = s"Parameter '$name' value is out of range. Value `$value` is not in [$lowerBound; $upperBound]"
}