package com.harana.sdk.backend.models.flow

import com.harana.sdk.backend.models.flow.graph.GraphKnowledge
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.shared.models.flow.{ActionInfo, ActionObjectInfo}

@SerialVersionUID(1L)
abstract class Action extends ActionInfo {

  def executeUntyped(l: List[ActionObjectInfo])(context: ExecutionContext): List[ActionObjectInfo]

  def inferKnowledgeUntyped(inputKnowledge: List[Knowledge[ActionObjectInfo]])(context: InferContext): (List[Knowledge[ActionObjectInfo]], InferenceWarnings)

  def inferGraphKnowledgeForInnerWorkflow(context: InferContext): GraphKnowledge = GraphKnowledge()

}