package com.harana.sdk.shared.models.flow.graph

import Edge.PortIndex
import com.harana.sdk.shared.models.flow.utils.Id
import io.circe.generic.JsonCodec

@JsonCodec
case class Edge(from: (Id, PortIndex), to: (Id, PortIndex))

object Edge {
  type PortIndex = Int
}