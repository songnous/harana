package com.harana.sdk.backend.models.flow.actions.exceptions

import com.harana.sdk.shared.models.flow.exceptions.{FlowError, HaranaError}

case class HaranaIOError(e: Throwable) extends FlowError {
  val message = s"Harana IO Exception: ${e.getMessage}"
  override val details = HaranaError.throwableToDetails(Some(e))
}