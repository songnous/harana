package com.harana.sdk.shared.models.flow.graph

import com.harana.sdk.shared.models.flow.graph.node.Node
import com.harana.sdk.shared.models.flow.{ActionInfo, graph}
import io.circe.generic.JsonCodec
import io.circe.{Decoder, Encoder, HCursor, Json}

import java.util.UUID

@JsonCodec
case class FlowGraph(override val nodes: Set[Node[ActionInfo]] = Set.empty,
                     override val edges: Set[Edge] = Set.empty)
    extends DirectedGraph[ActionInfo, FlowGraph](nodes, edges) {

  def subgraph(nodes: Set[Node[ActionInfo]], edges: Set[Edge]) = graph.FlowGraph(nodes, edges)
  def getDatasourcesIds = nodes.foldLeft(Set.empty[UUID])((acc, el) => acc ++ el.value.getDatasourcesIds)

}

object FlowGraph {

  implicit val flowNodeEncoder: Encoder[Node[ActionInfo]] = Encoder.instance[Node[ActionInfo]] { port =>
    Json.obj()
  }

  implicit val flowNodeDecoder: Decoder[Node[ActionInfo]] = (c: HCursor) =>
    null

}