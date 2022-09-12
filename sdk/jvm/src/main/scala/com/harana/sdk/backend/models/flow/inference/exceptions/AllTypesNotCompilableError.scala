package com.harana.sdk.backend.models.flow.inference.exceptions

import com.harana.sdk.shared.models.flow.exceptions.FlowError

case class AllTypesNotCompilableError(portIndex: Int) extends FlowError {
  val message = s"None of inferred types can be placed in the port $portIndex"
}
