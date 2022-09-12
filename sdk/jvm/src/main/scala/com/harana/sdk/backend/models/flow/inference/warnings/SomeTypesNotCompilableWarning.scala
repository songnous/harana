package com.harana.sdk.backend.models.flow.inference.warnings

import com.harana.sdk.backend.models.designer.flow.inference.InferenceWarning
import com.harana.sdk.backend.models.flow.inference.InferenceWarning

case class SomeTypesNotCompilableWarning(portIndex: Int) extends InferenceWarning {
  val message = s"Not all of inferred types can be placed in the port $portIndex"
}
