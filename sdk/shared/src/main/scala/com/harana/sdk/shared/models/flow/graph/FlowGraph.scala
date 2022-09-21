package com.harana.sdk.shared.models.flow.graph

import com.harana.sdk.shared.models.designer.flow
import FlowGraph.FlowNode
import com.harana.sdk.shared.models.flow.{ActionTypeInfo, graph}
import com.harana.sdk.shared.models.flow.graph.node.Node
import io.circe.{Decoder, Encoder, HCursor, Json}
import io.circe.generic.JsonCodec

import java.util.UUID

@JsonCodec
case class FlowGraph(override val nodes: Set[FlowNode] = Set.empty,
                     override val edges: Set[Edge] = Set())
    extends DirectedGraph[ActionTypeInfo, FlowGraph](nodes, edges) {

  def subgraph(nodes: Set[FlowNode], edges: Set[Edge]) = graph.FlowGraph(nodes, edges)
  def getDatasourcesIds = nodes.foldLeft(Set.empty[UUID])((acc, el) => acc ++ el.value.getDatasourcesIds)

}

object FlowGraph {
  type FlowNode = Node[ActionTypeInfo]

  	implicit val flowNodeEncoder: Encoder[FlowNode] = Encoder.instance[FlowNode] { port =>
  		Json.obj()
  	}

  	implicit val flowNodeDecoder: Decoder[FlowNode] = (c: HCursor) =>
      null
}