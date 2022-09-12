package com.harana.sdk.backend.models.flow.actions.exceptions

import com.harana.sdk.shared.models.flow.exceptions.FlowError

case class TooManyPossibleTypesError() extends FlowError {
  val message = "There is too many possible types. Parameters can not be fully validated."
}
