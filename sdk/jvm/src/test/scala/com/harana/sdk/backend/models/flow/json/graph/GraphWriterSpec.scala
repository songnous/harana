package com.harana.sdk.backend.models.flow.json.graph

import com.harana.models.json.graph.GraphJsonTestSupport
import com.harana.sdk.shared.models.designer.flow.graph.Endpoint
import com.harana.sdk.shared.models.flow.ActionTypeInfo
import com.harana.sdk.shared.models.flow.graph.{Edge, FlowGraph}
import com.harana.sdk.shared.models.flow.graph.node.Node

class GraphWriterSpec extends GraphJsonTestSupport {

  val action1 = mockAction(0, 1, ActionTypeInfo.Id.randomId, "name1")
  val action2 = mockAction(1, 1, ActionTypeInfo.Id.randomId, "name2")
  val action3 = mockAction(1, 1, ActionTypeInfo.Id.randomId, "name3")
  val action4 = mockAction(2, 1, ActionTypeInfo.Id.randomId, "name4")

  val node1 = Node(Node.Id.randomId, action1)
  val node2 = Node(Node.Id.randomId, action2)
  val node3 = Node(Node.Id.randomId, action3)
  val node4 = Node(Node.Id.randomId, action4)
  val nodes = Set(node1, node2, node3, node4)

  val edgesList = List((node1, node2, 0, 0), (node1, node3, 0, 0), (node2, node4, 0, 0), (node3, node4, 0, 1))
  val edges = edgesList.map(n => Edge(Endpoint(n._1.id, n._3), Endpoint(n._2.id, n._4))).toSet
  val graph = FlowGraph(nodes, edges)
  val graphJson = graph.asJson

  "Graph transformed to Json" should {
    "have 'nodes' field".which {

      "is a Seq" in {
        assert(graphJson.fields.contains("nodes"))
        assert(graphJson.fields("nodes").isInstanceOf[Seq])
      }

      "consists of all graph's nodes" in {
        val nodesArray      = graphJson.fields("nodes").asInstanceOf[Seq]
        val expectedNodeIds = Set(node1, node2, node3, node4).map(_.id.value.toString)
        val actualNodeIds   = nodesArray.elements
          .map(
            _.asJson
              .fields("id")
              .as[String]
          )
          .toSet
        assert(actualNodeIds == expectedNodeIds)
      }

      "have values created by NodeFormat" in {
        val nodesArray = graphJson.fields("nodes").asInstanceOf[Seq]
        val nodes = Set(node1, node2, node3, node4)
        assert(nodes.forall { node =>
          val nodeJson = node.asJson
          nodesArray.elements.count(jsValue => jsValue.asJson == nodeJson) == 1
        })
      }
    }
    "have 'edges' field".which {

      "is a Seq" in {
        assert(graphJson.fields.contains("connections"))
        assert(graphJson.fields("connections").isInstanceOf[Seq])
      }

      "consists of all graph's connections" in {
        val edgesArray = graphJson.fields("connections").asInstanceOf[Seq]
        assert(graph.edges.forall(edge => {
          edgesArray.elements.count {
            case edgeObject: Json =>
              endpointMatchesJson(edge.from, edgeObject.fields("from").asJson) &&
              endpointMatchesJson(edge.to, edgeObject.fields("to").asJson)
            case _                    => false
          } == 1
        }))
      }
    }
  }
}
