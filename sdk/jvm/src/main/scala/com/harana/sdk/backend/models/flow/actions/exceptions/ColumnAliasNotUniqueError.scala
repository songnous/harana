package com.harana.sdk.backend.models.flow.actions.exceptions

import com.harana.sdk.shared.models.flow.exceptions.FlowError

case class ColumnAliasNotUniqueError(alias: String) extends FlowError {
  val message = s"Alias '$alias' is not unique within the input DataFrame"
}
