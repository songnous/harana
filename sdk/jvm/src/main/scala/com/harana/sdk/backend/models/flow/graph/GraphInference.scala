package com.harana.sdk.backend.models.flow.graph

import com.harana.sdk.backend.models.flow.Knowledge
import com.harana.sdk.backend.models.flow.catalog.Catalog
import com.harana.sdk.backend.models.flow.graph.NodeInference.inputInferenceForNode
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.shared.models.flow.actionobjects.ActionObjectInfo
import com.harana.sdk.shared.models.flow.exceptions.{CyclicGraphError, HaranaError}
import com.harana.sdk.shared.models.flow.graph.TopologicallySortable
import com.harana.sdk.shared.models.flow.{Action, ActionTypeInfo}

case class SinglePortKnowledgeInferenceResult(knowledge: Knowledge[ActionObjectInfo],
                                              warnings: InferenceWarnings,
                                              errors: Seq[HaranaError])

object GraphInference {

   def inferKnowledge(graph: TopologicallySortable[Action[_ <: ActionTypeInfo]], context: InferContext, initialKnowledge: GraphKnowledge): GraphKnowledge = {
     graph.topologicallySorted.getOrElse(throw CyclicGraphError().toException)
      .filterNot(node => initialKnowledge.containsNodeKnowledge(node.id))
      .foldLeft(initialKnowledge) { (knowledge, node) =>
        val nodeInferenceResult = NodeInference.inferKnowledge(node, context, inputInferenceForNode(node, context, knowledge, graph.predecessors(node.id)))

        // FIXME: Should we be instantiation a new action type each time ?
        val innerWorkflowGraphKnowledge = Catalog.actionType(node.value.typeInfo)().inferGraphKnowledgeForInnerWorkflow(context)
        knowledge.addInference(node.id, nodeInferenceResult).addInference(innerWorkflowGraphKnowledge)
      }
  }
}