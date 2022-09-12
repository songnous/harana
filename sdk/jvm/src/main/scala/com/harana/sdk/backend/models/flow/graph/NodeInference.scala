package com.harana.sdk.backend.models.flow.graph

import com.harana.sdk.backend.models.designer.flow.Catalog.ActionObjectCatalog
import com.harana.sdk.backend.models.designer.flow.graph.TypesAccordance.TypesAccordance
import com.harana.sdk.backend.models.designer.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.designer.flow.{Action, Catalog, Knowledge}
import com.harana.sdk.backend.models.flow.Catalog
import com.harana.sdk.backend.models.flow.Catalog.ActionObjectCatalog
import com.harana.sdk.backend.models.flow.inference.InferenceWarnings
import com.harana.sdk.shared.models.flow.graph.FlowGraph.FlowNode
import com.harana.sdk.shared.models.designer.flow.graph._
import com.harana.sdk.shared.models.flow.ActionObjectInfo
import com.harana.sdk.shared.models.flow.exceptions.FlowError

import scala.reflect.runtime.universe.TypeTag

object NodeInference {

  def inferKnowledge(node: FlowNode, context: InferContext, inputInferenceForNode: NodeInferenceResult) = {

    val NodeInferenceResult(inKnowledge, warnings, errors) = inputInferenceForNode

    val parametersValidationErrors = node.value.validateParameters

    val action = Catalog.actionForActionInfo(node.value)

    def defaultInferenceResult(additionalErrors: Vector[FlowError] = Vector.empty) =
      createDefaultKnowledge(
        context.actionObjectCatalog,
        action,
        warnings,
        (errors ++ parametersValidationErrors ++ additionalErrors).distinct
      )

    if (parametersValidationErrors.nonEmpty) defaultInferenceResult()
    else {
      try {
        val (outKnowledge, inferWarnings) = action.inferKnowledgeUntyped(inKnowledge)(context)
        NodeInferenceResult(outKnowledge, warnings ++ inferWarnings, errors)
      } catch {
        case exception: FlowError => defaultInferenceResult(Vector(exception))
        case _: Exception => defaultInferenceResult()
      }
    }
  }

  def inputInferenceForNode(node: FlowNode, context: InferContext, graphKnowledge: GraphKnowledge, nodePredecessorsEndpoints: IndexedSeq[Option[Endpoint]]) = {
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

  private def inputKnowledgeAndAccordanceForInputPort(node: FlowNode, catalog: ActionObjectCatalog, graphKnowledge: GraphKnowledge, portIndex: Int, predecessorEndpointOption: Option[(Id, PortIndex])): (Knowledge[ActionObjectInfo], TypesAccordance) = {
    val inPortType = node.value.inPortTypes(portIndex).asInstanceOf[TypeTag[ActionObjectInfo]]
    predecessorEndpointOption match {
      case None => (KnowledgeService.defaultKnowledge(catalog, inPortType), TypesAccordance.NotProvided(portIndex))
      case Some(predecessorEndpoint) =>
        val outPortIndex = predecessorEndpoint._2
        val predecessorKnowledge = graphKnowledge.getKnowledge(predecessorEndpoint._1)(outPortIndex)
        inputKnowledgeAndAccordanceForInputPort(catalog, predecessorKnowledge, portIndex, inPortType)
    }
  }

  private def inputKnowledgeAndAccordanceForInputPort(catalog: ActionObjectCatalog, predecessorKnowledge: Knowledge[ActionObjectInfo], portIndex: Int, inPortType: TypeTag[ActionObjectInfo]): (Knowledge[ActionObjectInfo], TypesAccordance) = {
    val filteredTypes = predecessorKnowledge.filterTypes(inPortType.tpe)
    val filteredSize = filteredTypes.size
    if (filteredSize == predecessorKnowledge.size) (filteredTypes, TypesAccordance.All())
    else if (filteredSize == 0) (KnowledgeService.defaultKnowledge(catalog, inPortType), TypesAccordance.None(portIndex))
    else (filteredTypes, TypesAccordance.Some(portIndex))
  }

  private def createDefaultKnowledge(catalog: ActionObjectCatalog, action: Action, warnings: InferenceWarnings, errors: Vector[FlowError]) = {
    val outKnowledge = KnowledgeService.defaultOutputKnowledge(catalog, action)
    NodeInferenceResult(outKnowledge, warnings, errors)
  }
}