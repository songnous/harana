
package com.harana.sdk.shared.models.flow.parameters.validators

import com.harana.sdk.shared.models.flow.parameters.exceptions.{EmptyColumnNameError$, EmptyColumnPrefixNameError$, ValidationError}

trait ColumnNameStringValidator extends RegexValidatorLike {
  val regex = "[^`]+".r

  def exception: ValidationError

  override def validate(name: String, parameter: String) =
    if (parameter.nonEmpty) super.validate(name, parameter) else Vector(exception)
}

object ColumnNameValidator extends ColumnNameStringValidator {
  def exception: ValidationError = EmptyColumnNameError$
}

object ColumnPrefixNameValidator extends ColumnNameStringValidator {
  def exception: ValidationError = EmptyColumnPrefixNameError$
}