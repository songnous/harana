package com.harana.sdk.backend.models.flow.graph

import com.harana.sdk.backend.models.flow.Catalog.ActionObjectCatalog
import com.harana.sdk.backend.models.flow.actiontypes.ActionType
import com.harana.sdk.backend.models.flow.graph.TypesAccordance.TypesAccordance
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.flow.{Catalog, Knowledge}
import com.harana.sdk.shared.models.flow.{Action, ActionTypeInfo}
import com.harana.sdk.shared.models.flow.actionobjects.ActionObjectInfo
import com.harana.sdk.shared.models.flow.exceptions.FlowError
import com.harana.sdk.shared.models.flow.graph.Endpoint
import com.harana.sdk.shared.models.flow.graph.node.Node

import izumi.reflect.Tag

object NodeInference {

  def inferKnowledge(node: Node[Action[_ <: ActionTypeInfo]], context: InferContext, inputInferenceForNode: NodeInferenceResult) = {

    val NodeInferenceResult(inKnowledge, warnings, errors) = inputInferenceForNode

    val parametersValidationErrors = node.value.typeInfo.validateParameters

    val actionType = Catalog.actionTypeForActionTypeInfo(node.value.typeInfo)

    def defaultInferenceResult(additionalErrors: List[FlowError] = List.empty) =
      createDefaultKnowledge(
        context.actionObjectCatalog,
        actionType,
        warnings,
        (errors ++ parametersValidationErrors ++ additionalErrors).distinct
      )

    if (parametersValidationErrors.nonEmpty) defaultInferenceResult()
    else {
      try {
        val (outKnowledge, inferWarnings) = actionType.inferKnowledgeUntyped(inKnowledge)(context)
        NodeInferenceResult(outKnowledge, warnings ++ inferWarnings, errors)
      } catch {
        case exception: FlowError => defaultInferenceResult(List(exception))
        case _: Exception => defaultInferenceResult()
      }
    }
  }

  def inputInferenceForNode(node: Node[Action[_ <: ActionTypeInfo]], context: InferContext, graphKnowledge: GraphKnowledge, nodePredecessorsEndpoints: IndexedSeq[Option[Endpoint]]) = {
    (0 until node.value.inArity).foldLeft(NodeInferenceResult.empty) {
      case (NodeInferenceResult(knowledge, warnings, errors), portIndex) =>
        val predecessorEndpoint = nodePredecessorsEndpoints(portIndex)
        val (portKnowledge, accordance) =
          inputKnowledgeAndAccordanceForInputPort(
            node,
            context.actionObjectCatalog,
            graphKnowledge,
            portIndex,
            predecessorEndpoint
          )
        NodeInferenceResult(
          knowledge :+ portKnowledge,
          warnings ++ accordance.warnings,
          errors ++ accordance.errors
        )
    }
  }

  private def inputKnowledgeAndAccordanceForInputPort(node: Node[Action[_ <: ActionTypeInfo]], catalog: ActionObjectCatalog, graphKnowledge: GraphKnowledge, portIndex: Int, predecessorEndpointOption: Option[Endpoint]): (Knowledge[ActionObjectInfo], TypesAccordance) = {
    val inPortType = node.value.typeInfo.inputPorts(portIndex).asInstanceOf[Tag[ActionObjectInfo]]
    predecessorEndpointOption match {
      case None => (KnowledgeService.defaultKnowledge(catalog, inPortType), TypesAccordance.NotProvided(portIndex))
      case Some(predecessorEndpoint) =>
        val outPortIndex = predecessorEndpoint.portIndex
        val predecessorKnowledge = graphKnowledge.getKnowledge(predecessorEndpoint.nodeId)(outPortIndex)
        inputKnowledgeAndAccordanceForInputPort(catalog, predecessorKnowledge, portIndex, inPortType)
    }
  }

  private def inputKnowledgeAndAccordanceForInputPort(catalog: ActionObjectCatalog, predecessorKnowledge: Knowledge[ActionObjectInfo], portIndex: Int, inPortType: Tag[ActionObjectInfo]): (Knowledge[ActionObjectInfo], TypesAccordance) = {
    val filteredTypes = predecessorKnowledge.filterTypes(inPortType.tpe)
    val filteredSize = filteredTypes.size
    if (filteredSize == predecessorKnowledge.size) (filteredTypes, TypesAccordance.All())
    else if (filteredSize == 0) (KnowledgeService.defaultKnowledge(catalog, inPortType), TypesAccordance.None(portIndex))
    else (filteredTypes, TypesAccordance.Some(portIndex))
  }

  private def createDefaultKnowledge(catalog: ActionObjectCatalog, action: ActionType, warnings: InferenceWarnings, errors: List[FlowError]) = {
    val outKnowledge = KnowledgeService.defaultOutputKnowledge(catalog, action)
    NodeInferenceResult(outKnowledge, warnings, errors)
  }
}