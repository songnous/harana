package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.exceptions.FlowError
import com.harana.sdk.shared.models.flow.parameters.validators.Validator

trait HasValidator[T] extends Parameter[T] {

  val validator: Validator[T]

  override def validate(value: T): List[FlowError] = validator.validate(name, value)

  override def constraints = if (validator.toHumanReadable(name).isEmpty) "" else " " + validator.toHumanReadable(name)

}
