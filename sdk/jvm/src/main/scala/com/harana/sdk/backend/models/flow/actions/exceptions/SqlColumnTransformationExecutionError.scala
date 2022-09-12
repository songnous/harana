package com.harana.sdk.backend.models.flow.actions.exceptions

import com.harana.sdk.shared.models.flow.exceptions.{ActionExecutionError, HaranaError}

case class SqlColumnTransformationExecutionError(
    inputColumnName: String,
    formula: String,
    outputColumnName: String,
    rootCause: Option[Throwable]
) extends ActionExecutionError {
  val message = s"Problem while executing SqlColumnTransformation with the following formula: '$formula'"
  override val details = HaranaError.throwableToDetails(rootCause)
}