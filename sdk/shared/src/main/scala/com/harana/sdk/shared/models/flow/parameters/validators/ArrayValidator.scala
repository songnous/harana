package com.harana.sdk.shared.models.flow.parameters.validators

case class ArrayValidator[T](validator: Validator[T]) extends Validator[Array[T]] {

  override def validate(name: String, parameter: Array[T]) = {
    parameter.collect(validator.validate(name, _)).flatten.toList
  }

}