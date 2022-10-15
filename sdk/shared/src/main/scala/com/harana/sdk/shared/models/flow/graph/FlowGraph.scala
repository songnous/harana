package com.harana.sdk.shared.models.flow.graph

import com.harana.sdk.shared.models.flow.graph.node.Node
import com.harana.sdk.shared.models.flow.{Action, ActionTypeInfo, graph}
import io.circe.generic.JsonCodec

@JsonCodec
case class FlowGraph(override val nodes: Set[Node[Action[_ <: ActionTypeInfo]]] = Set.empty,
                     override val edges: Set[Edge] = Set.empty)
    extends DirectedGraph[Action[_ <: ActionTypeInfo], FlowGraph](nodes, edges) {

  def subgraph(nodes: Set[Node[Action[_ <: ActionTypeInfo]]], edges: Set[Edge]) = graph.FlowGraph(nodes, edges)

}
//
//object FlowGraph {
//
//  implicit val nodeEncoder: Encoder[Node[Action[_]]] = Encoder.instance[Node[ActionTypeInfo]] { port =>
//    Json.obj()
//  }
//
//  implicit val nodeDecoder: Decoder[Node[Action[_]]] = (c: HCursor) =>
//    null
//
//}