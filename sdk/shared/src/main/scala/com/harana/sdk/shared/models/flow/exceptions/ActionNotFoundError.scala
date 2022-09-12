package com.harana.sdk.shared.models.flow.exceptions

import com.harana.sdk.shared.models.flow.ActionInfo

case class ActionNotFoundError(actionId: ActionInfo.Id) extends ActionsCatalogError {
  val message = s"Action not found: $actionId"
}
