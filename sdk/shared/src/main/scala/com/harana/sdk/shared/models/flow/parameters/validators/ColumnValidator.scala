
package com.harana.sdk.shared.models.flow.parameters.validators

import com.harana.sdk.shared.models.common.ParameterValue
import com.harana.sdk.shared.models.flow.parameters.exceptions.{EmptyColumnNameError$, EmptyColumnPrefixNameError$, ValidationError}
import io.circe.generic.JsonCodec

trait ColumnValidator extends RegexValidator {
  val regex = "[^`]+".r

  def exception: ValidationError

  override def validate(name: String, parameter: String) =
    if (parameter.nonEmpty) super.validate(name, parameter) else List(exception)
}

object ColumnValidator {
  object Name extends ColumnValidator {
    def exception: ValidationError = EmptyColumnNameError$
  }

  object PrefixName extends ColumnValidator {
    def exception: ValidationError = EmptyColumnPrefixNameError$
  }
}
