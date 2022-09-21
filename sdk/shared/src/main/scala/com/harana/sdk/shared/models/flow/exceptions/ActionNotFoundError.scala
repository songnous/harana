package com.harana.sdk.shared.models.flow.exceptions

import com.harana.sdk.shared.models.flow.ActionTypeInfo

case class ActionNotFoundError(actionId: ActionTypeInfo.Id) extends ActionsCatalogError {
  val message = s"Action not found: $actionId"
}
