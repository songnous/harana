package com.harana.workflowexecutor

import com.harana.sdk.backend.models.designer.flow.actions.exceptions.EmptyDataframeError$
import com.harana.sdk.shared.models.designer.flow.exceptions.FlowError

// Unfortunetely Spark exceptions are stringly typed. Spark does not have their exception classes. This extractor hides Sparks strings and converts spark exceptions to flows.
object SparkExceptionAsDeeplangException {

  def unapply(exception: Exception): Option[FlowError] = exception match {
    case emptyCollectionEx if emptyCollectionEx.getMessage == "empty collection" => Some(EmptyDataframeError$)
    case _ => None
  }

}
