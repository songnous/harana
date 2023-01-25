package com.harana.sdk.shared.models.flow.parameters.custom

import com.harana.sdk.shared.models.flow.{Action, ActionTypeInfo}
import com.harana.sdk.shared.models.flow.actiontypes.custom.{SinkInfo, SourceInfo}
import com.harana.sdk.shared.models.flow.graph.FlowGraph
import com.harana.sdk.shared.models.flow.graph.node.Node
import com.harana.sdk.shared.utils.HMap
import io.circe.Json
import io.circe.generic.JsonCodec

@JsonCodec
case class InnerWorkflow(graph: FlowGraph,
                         thirdPartyData: Json = Json.Null,
                         publicParameters: List[PublicParameter] = List.empty) {

  val sourceId = new SourceInfo {}.id
  val sinkId = new SinkInfo {}.id

  require(findNodeOfType(sourceId).nonEmpty, "Inner workflow must have source node")
  require(findNodeOfType(sinkId).nonEmpty, "Inner workflow must have sink node")

  val source = findNodeOfType(sourceId).get
  val sink = findNodeOfType(sinkId).get

  private def findNodeOfType(actionId: ActionTypeInfo.Id) =
    graph.nodes.find(_.value.id == actionId)

}

object InnerWorkflow {
  val sourceAction = Action(new SourceInfo {}, (0,0), None, None, None, HMap.empty)
  val sinkAction = Action(new SinkInfo {}, (0,0), None, None, None, HMap.empty)
  val empty = InnerWorkflow(FlowGraph(Set(Node(sourceAction.id, sourceAction), Node(sinkAction.id, sinkAction))), Json.Null)
}

@JsonCodec
case class PublicParameter(nodeId: Node.Id, parameterName: String, publicName: String)
