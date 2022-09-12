package com.harana.sdk.backend.models.flow.inference.exceptions

import com.harana.sdk.shared.models.flow.exceptions.FlowError

case class NoInputEdgesError(portIndex: Int) extends FlowError {
  val message = s"Nothing is connected to the port $portIndex"
}
