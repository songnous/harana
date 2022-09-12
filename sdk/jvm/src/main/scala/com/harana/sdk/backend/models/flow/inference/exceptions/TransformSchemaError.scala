package com.harana.sdk.backend.models.flow.inference.exceptions

import com.harana.sdk.shared.models.flow.exceptions.FlowError
import com.harana.sdk.shared.models.flow.exceptions.HaranaError.throwableToDetails

case class TransformSchemaError(message: String, e: Option[Exception] = None) extends FlowError {
  override val details = throwableToDetails(e)
}