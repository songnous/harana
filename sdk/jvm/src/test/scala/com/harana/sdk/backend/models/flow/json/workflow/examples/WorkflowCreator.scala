package com.harana.sdk.backend.models.flow.json.workflow.examples

import com.harana.sdk.shared.models.designer.flow.flows.{Workflow, WorkflowWithVariables}
import com.harana.sdk.backend.models.flow.actiontypes.ActionType
import com.harana.sdk.shared.models.designer.flow.Variables
import com.harana.sdk.shared.models.flow.FlowType
import com.harana.sdk.shared.models.flow.graph.FlowGraph.Node[ActionTypeInfo]
import com.harana.sdk.shared.models.flow.graph.{Edge, FlowGraph}
import com.harana.sdk.shared.models.flow.graph.node.Node
import io.circe.Json
import io.circe.syntax.EncoderOps

import java.util.UUID

abstract class WorkflowCreator {

  val apiVersion = "0.4.0"

  def nodes: Seq[Node[ActionTypeInfo]]
  def edges: Seq[Edge]
  def experimentName: String
  def node(action: ActionType): Node[ActionTypeInfo] = Node(UUID.randomUUID(), action)

  def buildWorkflow() = {
    val graph = FlowGraph(nodes.toSet, edges.toSet)
    val thirdPartyData = Json.Null
    val variables = Variables()
    val result = WorkflowWithVariables(Workflow.Id.randomId, FlowType.Batch, apiVersion, graph, thirdPartyData, variables)
    println(result.asJson.noSpaces)
    result
  }
}
