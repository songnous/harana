package com.harana.workflowexecutor

import com.harana.sdk.backend.models.designer.flow.{CatalogRecorder, TestSupport, IntegratedTestSupport, graph}
import com.harana.sdk.backend.models.designer.flow.graph.{KnowledgeService, NodeInference, NodeInferenceResult}
import com.harana.sdk.shared.models.designer.flow.graph.node.Node
import com.harana.sdk.shared.models.designer.flow.ActionInfo
import com.harana.sdk.shared.models.designer.flow.catalogs.FlowCatalog

class InferKnowledgeIntegratedTest extends IntegratedTestSupport with TestSupport {

  val nodeInference = new NodeInference {}
  val FlowCatalog(_, actionObjectCatalog, actionsCatalog) = CatalogRecorder.resourcesCatalogRecorder.catalogs
  val inferCtx = createInferContext(actionObjectCatalog)

  for (action <- actionsCatalog.actions.values) {
    action.name should {
      "not throw in inferKnowledge" in {
        val op = actionsCatalog.createAction(action.id)
        val opNode = Node[Action](action.id, op)
        val inputKnowledge = KnowledgeService.defaultInputKnowledge(actionObjectCatalog, op)
        noException should be thrownBy nodeInference.inferKnowledge(opNode, inferCtx, graph.NodeInferenceResult(inputKnowledge))
      }
    }
  }
}