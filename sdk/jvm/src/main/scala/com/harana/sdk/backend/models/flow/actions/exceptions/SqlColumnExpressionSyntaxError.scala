package com.harana.sdk.backend.models.flow.actions.exceptions

import com.harana.sdk.shared.models.flow.exceptions.FlowError
import io.circe.generic.JsonCodec

@JsonCodec
case class SqlColumnExpressionSyntaxError(formula: String) extends FlowError {
  val message = s"Formula: '$formula' is not a valid Spark SQL expression"
}
