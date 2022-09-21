package com.harana.sdk.backend.models.flow.graph

import com.harana.sdk.backend.models.flow.graph.NodeInference.inputInferenceForNode
import com.harana.sdk.backend.models.flow.{Catalog, Knowledge}
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.shared.models.flow.exceptions.{CyclicGraphError, HaranaError}
import com.harana.sdk.shared.models.flow.graph.TopologicallySortable
import com.harana.sdk.shared.models.flow.{ActionTypeInfo, ActionObjectInfo}

case class SinglePortKnowledgeInferenceResult(knowledge: Knowledge[ActionObjectInfo],
                                              warnings: InferenceWarnings,
                                              errors: Seq[HaranaError])

object GraphInference {

   def inferKnowledge(graph: TopologicallySortable[ActionTypeInfo], context: InferContext, initialKnowledge: GraphKnowledge): GraphKnowledge = {
     graph.topologicallySorted.getOrElse(throw CyclicGraphError().toException)
      .filterNot(node => initialKnowledge.containsNodeKnowledge(node.id))
      .foldLeft(initialKnowledge) { (knowledge, node) =>
        val nodeInferenceResult = NodeInference.inferKnowledge(node, context, inputInferenceForNode(node, context, knowledge, graph.predecessors(node.id)))
        val innerWorkflowGraphKnowledge = Catalog.actionForActionInfo(node.value).inferGraphKnowledgeForInnerWorkflow(context)
        knowledge.addInference(node.id, nodeInferenceResult).addInference(innerWorkflowGraphKnowledge)
      }
  }
}