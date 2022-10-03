package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow.graph.GraphKnowledge
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.flow.{ExecutionContext, Knowledge}
import com.harana.sdk.shared.models.flow.ActionTypeInfo
import com.harana.sdk.shared.models.flow.actionobjects.ActionObjectInfo

@SerialVersionUID(1L)
abstract class ActionType extends ActionTypeInfo {

  def executeUntyped(l: List[ActionObjectInfo])(context: ExecutionContext): List[ActionObjectInfo]

  def inferKnowledgeUntyped(inputKnowledge: List[Knowledge[ActionObjectInfo]])(context: InferContext): (List[Knowledge[ActionObjectInfo]], InferenceWarnings)

  def inferGraphKnowledgeForInnerWorkflow(context: InferContext): GraphKnowledge = GraphKnowledge()

}