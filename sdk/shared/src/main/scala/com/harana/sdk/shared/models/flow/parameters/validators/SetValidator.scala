package com.harana.sdk.shared.models.flow.parameters.validators

case class SetValidator[T](validator: Validator[T]) extends Validator[Set[T]] {

  override def validate(name: String, parameter: Set[T]) = {
    parameter.collect(validator.validate(name, _)).flatten.toList
  }

}