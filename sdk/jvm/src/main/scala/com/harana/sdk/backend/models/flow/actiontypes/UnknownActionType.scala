package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow.{ExecutionContext, Knowledge}
import com.harana.sdk.backend.models.flow.actiontypes.exceptions.UnknownActionExecutionError
import com.harana.sdk.backend.models.flow.inference.InferContext
import com.harana.sdk.shared.models.flow.actionobjects.ActionObjectInfo
import com.harana.sdk.shared.models.flow.actiontypes.UnknownActionInfo

class UnknownActionType extends ActionType with UnknownActionInfo {

  def executeUntyped(arguments: List[ActionObjectInfo])(context: ExecutionContext) =
    throw UnknownActionExecutionError().toException

  override def inferKnowledgeUntyped(knowledge: List[Knowledge[ActionObjectInfo]])(context: InferContext) =
    throw UnknownActionExecutionError().toException

}
