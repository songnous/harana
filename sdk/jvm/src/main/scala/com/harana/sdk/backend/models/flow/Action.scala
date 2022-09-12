package com.harana.sdk.backend.models.flow

import com.harana.sdk.backend.models.flow.graph.GraphKnowledge
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.shared.models.flow.{ActionInfo, ActionObjectInfo}

@SerialVersionUID(1L)
abstract class Action extends ActionInfo {

  def executeUntyped(l: Vector[ActionObjectInfo])(context: ExecutionContext): Vector[ActionObjectInfo]

  def inferKnowledgeUntyped(inputKnowledge: Vector[Knowledge[ActionObjectInfo]])(context: InferContext): (Vector[Knowledge[ActionObjectInfo]], InferenceWarnings)

  def inferGraphKnowledgeForInnerWorkflow(context: InferContext): GraphKnowledge = GraphKnowledge()

}