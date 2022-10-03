package com.harana.sdk.backend.models.flow.actiontypes.exceptions

import com.harana.sdk.shared.models.flow.exceptions.{FlowError, HaranaError}

case class HaranaUnknownHostError(e: Throwable) extends FlowError {
  val message = s"Unknown host: ${e.getMessage}"
  override val details = HaranaError.throwableToDetails(Some(e))
}