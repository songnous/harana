package com.harana.sdk.shared.models.flow.graph

import com.harana.sdk.shared.models.flow.graph.node.Node
import io.circe.generic.JsonCodec

@JsonCodec
case class Edge(from: Endpoint, to: Endpoint)

object Edge {
  type PortIndex = Int

  def apply(from: (Node[_], Int), to: (Node[_], Int)): Edge =
    Edge(Endpoint(from._1.id, from._2), Endpoint(to._1.id, to._2))
}

@JsonCodec
case class Endpoint(nodeId: Node.Id, portIndex: Int)