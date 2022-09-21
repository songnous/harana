package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.flow.{ActionType, ExecutionContext, Knowledge}
import com.harana.sdk.backend.models.flow.actions.exceptions.UnknownActionExecutionError
import com.harana.sdk.backend.models.flow.inference.InferContext
import com.harana.sdk.shared.models.flow.ActionObjectInfo
import com.harana.sdk.shared.models.flow.actions.UnknownActionInfo

class UnknownAction extends ActionType with UnknownActionInfo {

  def executeUntyped(arguments: List[ActionObjectInfo])(context: ExecutionContext) =
    throw UnknownActionExecutionError().toException

  override def inferKnowledgeUntyped(knowledge: List[Knowledge[ActionObjectInfo]])(context: InferContext) =
    throw UnknownActionExecutionError().toException

}
