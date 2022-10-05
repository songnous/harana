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

  val sourceId = SourceInfo.id
  val sinkId = SinkInfo.id

  require(findNodeOfType(sourceId).isDefined, "Inner workflow must have source node")
  require(findNodeOfType(sinkId).isDefined, "Inner workflow must have sink node")

  val source = findNodeOfType(sourceId).get
  val sink = findNodeOfType(sinkId).get

  // FIXME
  private def findNodeOfType(actionId: ActionTypeInfo.Id) = graph.nodes.find(_.value.id == actionId.toString)

}

object InnerWorkflow {
  val sourceAction = Action((0,0), SourceInfo.inArity, SourceInfo.outArity, None, None, None, HMap.empty)
  val sinkAction = Action((0,0), SinkInfo.inArity, SinkInfo.outArity, None, None, None, HMap.empty)
  val empty = InnerWorkflow(FlowGraph(Set(Node(sourceAction.id, sourceAction), Node(sinkAction.id, sinkAction))), Json.Null)
}

@JsonCodec
case class PublicParameter(nodeId: Node.Id, parameterName: String, publicName: String)
