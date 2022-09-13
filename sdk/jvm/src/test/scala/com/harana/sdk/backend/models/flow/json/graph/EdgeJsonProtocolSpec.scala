package com.harana.sdk.backend.models.flow.json.graph

import com.harana.sdk.backend.models.flow.json.graph.EdgeJsonProtocol._
import com.harana.models.json.graph.GraphJsonTestSupport
import com.harana.sdk.shared.models.designer.flow.graph.{Endpoint}
import com.harana.sdk.shared.models.flow.graph.Edge
import com.harana.sdk.shared.models.flow.graph.node.Node
import io.circe.syntax.EncoderOps

class EdgeJsonProtocolSpec extends GraphJsonTestSupport {

  val expectedFromId: Node.Id = Node.Id.randomId
  val expectedFromPort = 1989

  val expectedToId: Node.Id = Node.Id.randomId
  val expectedToPort = 1337

  val edge = Edge(
    Endpoint(expectedFromId, expectedFromPort),
    Endpoint(expectedToId, expectedToPort)
  )

  "Edge transformed to Json" should {
    "have correct from and to" in {
      val edgeJson = edge.asJson
      assertEndpointMatchesJson(edge.from, edgeJson.fields("from").asJson)
      assertEndpointMatchesJson(edge.to, edgeJson.fields("to").asJson)
    }
  }

  "Edge transformed to Json and then read to Object" should {
    "be equal" in {
      edge.asJson.as[Edge] shouldBe edge
    }
  }
}
