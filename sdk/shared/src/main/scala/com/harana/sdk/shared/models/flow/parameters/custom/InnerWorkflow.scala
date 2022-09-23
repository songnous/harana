package com.harana.sdk.shared.models.flow.parameters.custom

import com.harana.sdk.shared.models.flow.ActionInfo
import com.harana.sdk.shared.models.flow.actions.custom.{SinkInfo, SourceInfo}
import com.harana.sdk.shared.models.flow.graph.FlowGraph
import com.harana.sdk.shared.models.flow.graph.FlowGraph.FlowNode
import com.harana.sdk.shared.models.flow.graph.node.Node
import io.circe.Json
import io.circe.generic.JsonCodec

@JsonCodec
case class InnerWorkflow(graph: FlowGraph,
                         thirdPartyData: Json = Json.Null,
                         publicParameters: List[PublicParameter] = List.empty) {

  val sourceId = SourceInfo.id
  val sinkId = SinkInfo.id

  require(findNodeOfType(sourceId).isDefined, "Inner workflow must have source node")
  require(findNodeOfType(sinkId).isDefined, "Inner workflow must have sink node")

  val source: FlowNode = findNodeOfType(sourceId).get
  val sink: FlowNode = findNodeOfType(sinkId).get

  private def findNodeOfType(actionId: ActionInfo.Id) = graph.nodes.find(_.value.id == actionId)
  def getDatasourcesIds = graph.getDatasourcesIds

}

object InnerWorkflow {
  val empty = InnerWorkflow(FlowGraph(Set(Node(Node.Id.randomId, new SourceInfo() {}), Node(Node.Id.randomId, new SinkInfo() {}))), Json.Null)
}

@JsonCodec
case class PublicParameter(nodeId: Node.Id, parameterName: String, publicName: String)
