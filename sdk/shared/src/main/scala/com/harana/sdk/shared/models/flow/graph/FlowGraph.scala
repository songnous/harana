package com.harana.sdk.shared.models.flow.graph

import com.harana.sdk.shared.models.flow.graph.node.Node
import com.harana.sdk.shared.models.flow.{ActionTypeInfo, graph}
import io.circe.generic.JsonCodec
import io.circe.{Decoder, Encoder, HCursor, Json}

import java.util.UUID

@JsonCodec
case class FlowGraph(override val nodes: Set[Node[ActionTypeInfo]] = Set.empty,
                     override val edges: Set[Edge] = Set.empty)
    extends DirectedGraph[ActionTypeInfo, FlowGraph](nodes, edges) {

  def subgraph(nodes: Set[Node[ActionTypeInfo]], edges: Set[Edge]) = graph.FlowGraph(nodes, edges)
  def getDatasourcesIds = nodes.foldLeft(Set.empty[UUID])((acc, el) => acc ++ el.value.getDatasourcesIds)

}

object FlowGraph {

  implicit val nodeEncoder: Encoder[Node[ActionTypeInfo]] = Encoder.instance[Node[ActionTypeInfo]] { port =>
    Json.obj()
  }

  implicit val nodeDecoder: Decoder[Node[ActionTypeInfo]] = (c: HCursor) =>
    null

}