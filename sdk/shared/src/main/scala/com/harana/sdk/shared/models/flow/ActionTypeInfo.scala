package com.harana.sdk.shared.models.flow

import com.harana.sdk.shared.models.common.ParameterGroup
import com.harana.sdk.shared.models.designer.flow.actiontypes.ActionTypeGroup

trait ActionTypeInfo {
  val tags: Set[String]
  val group: ActionTypeGroup
  val inputPorts: List[Port]
  val outputPorts: List[Port]
  val parameterGroups: List[ParameterGroup]
  val mode: ActionMode = ActionMode.Normal
}
