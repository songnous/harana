package com.harana.sdk.backend.models.flow.graph

import com.harana.sdk.backend.models.flow.graph.RandomNodeFactory._
import com.harana.sdk.backend.models.flow.utils.{Logging, Serialization}
import com.harana.sdk.shared.models.flow
import com.harana.sdk.shared.models.flow.graph
import com.harana.sdk.shared.models.flow.graph.FlowGraph.FlowNode
import com.harana.sdk.shared.models.flow.graph.node.Node
import com.harana.sdk.shared.models.flow.graph.{Edge, FlowGraph}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar

class DirectedGraphSpec extends AnyFunSuite with Matchers with Serialization with Logging with MockitoSugar with GraphTestSupport {

  test("An empty Graph should have size 0") {
    FlowGraph().size shouldBe 0
  }

  test("An edge added to an empty graph should be filtered out as invalid") {
    val edge  = Edge((Node.Id.randomId, 0), (Node.Id.randomId, 0))
    val graph = FlowGraph(Set(), Set(edge))
    graph.getValidEdges shouldBe Set()
  }

  test("Graph with two nodes should have size 2") {
    import com.harana.sdk.backend.models.flow.graph.ActionTestClasses._

    val node1 = randomNode(ActionTypeA1ToA())
    val node2 = randomNode(ActionTypeA1ToA())
    val nodes = Set(node1, node2)
    val edges = Set(Edge((node1, 0), (node2, 0)))
    val graph = flow.graph.FlowGraph(nodes, edges)
    graph.size shouldBe 2
  }

  test("Programmer can validate if graph doesn't contain a cycle") {
    import com.harana.sdk.backend.models.flow.graph.ActionTestClasses._

    val node1 = randomNode(ActionTypeA1ToA())
    val node2 = randomNode(ActionTypeA1A2ToA())
    val node3 = randomNode(ActionTypeA1ToA())
    val node4 = randomNode(ActionTypeA1ToA())
    val nodes = Set(node1, node2, node3, node4)
    val nonCyclicEdges = Set(
      Edge((node1, 0), (node2, 0)),
      Edge((node2, 0), (node3, 0)),
      Edge((node3, 0), (node4, 0))
    )
    var graph = FlowGraph(nodes, nonCyclicEdges)
    assert(!graph.containsCycle)
    graph = graph.copy(edges = graph.edges + Edge((node4.id, 0), (node2.id, 1)))
    assert(graph.containsCycle)
  }

  test("Simple Graph can be sorted topologically") {
    import com.harana.sdk.backend.models.flow.graph.ActionTestClasses._

    val node1 = randomNode(ActionTypeA1ToA())
    val node2 = randomNode(ActionTypeA1ToA())
    val node3 = randomNode(ActionTypeA1ToA())
    val node4 = randomNode(ActionTypeA1ToA())
    val edges = Set(Edge((node1, 0), (node2, 0)), Edge((node2, 0), (node3, 0)), Edge((node3, 0), (node4, 0)))
    val graph  = flow.graph.FlowGraph(Set(node1, node2, node3, node4), edges)
    val sorted = graph.topologicallySorted
    assert(sorted.contains(List(node1, node2, node3, node4)))
  }

  test("Simple Graph can calculate its direct and non-direct precedessors") {
    import com.harana.sdk.backend.models.flow.graph.ActionTestClasses._

    val node1 = randomNode(ActionTypeA1ToA())
    val node2 = randomNode(ActionTypeA1ToA())
    val node3 = randomNode(ActionTypeA1ToA())
    val node4 = randomNode(ActionTypeA1ToA())
    val edges = Set(Edge((node1, 0), (node2, 0)), Edge((node2, 0), (node3, 0)), Edge((node3, 0), (node4, 0)))

    val graph = flow.graph.FlowGraph(Set(node1, node2, node3, node4), edges)

    val predsOfNode3 = graph.allPredecessorsOf(node3.id)
    assert(predsOfNode3 == Set(node1, node2))
  }

  test("Complicated Graph can be sorted topologically") {
    import com.harana.sdk.backend.models.flow.graph.ActionTestClasses._

    def checkIfInOrder(node1: FlowNode, node2: FlowNode, order: List[FlowNode]) =
      assert(order.indexOf(node1) < order.indexOf(node2))

    val node1 = randomNode(ActionTypeA1ToA())
    val node2 = randomNode(ActionTypeA1ToA())
    val node3 = randomNode(ActionTypeA1ToA())
    val node4 = randomNode(ActionTypeA1A2ToA())
    val node5 = randomNode(ActionTypeA1ToA())
    val node6 = randomNode(ActionTypeA1ToA())
    val node7 = randomNode(ActionTypeA1A2ToA())
    val nodes = Set(node1, node2, node3, node4, node5, node6, node7)
    val edges = List(
      (node1, node2, 0, 0),
      (node1, node3, 0, 0),
      (node2, node4, 0, 0),
      (node3, node4, 0, 1),
      (node4, node5, 0, 0),
      (node4, node6, 0, 0),
      (node5, node7, 0, 0),
      (node6, node7, 0, 1)
    )
    val edgesSet = edges.map(n => Edge((n._1.id, n._3), (n._2.id, n._4))).toSet
    val graph = flow.graph.FlowGraph(nodes, edgesSet)

    val sortedOption = graph.topologicallySorted
    assert(sortedOption.isDefined)
    val sorted = sortedOption.get
    edges.foreach(n => checkIfInOrder(n._1, n._2, sorted))
  }

  test("Graph's nodes have correct predecessors and successors") {
    import com.harana.sdk.backend.models.flow.graph.ActionTestClasses._

    val node1 = randomNode(ActionTypeCreateA1())
    val node2 = randomNode(ActionTypeA1ToA())
    val node3 = randomNode(ActionTypeA1ToA())
    val node4 = randomNode(ActionTypeA1A2ToA())
    val nodes = Set(node1, node2, node3, node4)
    val edges = Set(
      Edge((node1, 0), (node2, 0)),
      Edge((node1, 0), (node3, 0)),
      Edge((node2, 0), (node4, 0)),
      Edge((node3, 0), (node4, 1))
    )
    val graph = flow.graph.FlowGraph(nodes, edges)

    graph.predecessors(node1.id).size shouldBe 0
    graph.predecessors(node2.id) should contain theSameElementsAs List(Some((node1.id, 0)))
    graph.predecessors(node3.id) should contain theSameElementsAs List(Some((node1.id, 0)))
    graph.predecessors(node4.id) should contain theSameElementsAs List(Some((node2.id, 0)), Some((node3.id, 0)))

    graph.successors(node1.id) should contain theSameElementsAs List(Set((node2.id, 0), (node3.id, 0)))
    graph.successors(node2.id) should contain theSameElementsAs List(Set((node4.id, 0)))
    graph.successors(node3.id) should contain theSameElementsAs List(Set((node4.id, 1)))
    graph.successors(node4.id) should contain theSameElementsAs List(Set.empty)
  }

  test("Graph allows to calculate a subgraph") {
    FlowGraph().subgraph(Set()) should have size 0

    val bigGraph = graph.FlowGraph(nodeSet, edgeSet)
    bigGraph.subgraph(nodeSet.map(_.id)) shouldBe bigGraph
    bigGraph.subgraph(Set(idA)) shouldBe FlowGraph(Set(nodeA), Set())
    bigGraph.subgraph(Set(idA, idB)) shouldBe FlowGraph(Set(nodeA, nodeB), Set(edge1))
    bigGraph.subgraph(Set(idD)) shouldBe FlowGraph(Set(nodeA, nodeB, nodeC, nodeD), Set(edge1, edge2, edge3))
    bigGraph.subgraph(Set(idE, idC)) shouldBe FlowGraph(Set(nodeE, nodeC, nodeB, nodeA), Set(edge1, edge2, edge4, edge5))
    bigGraph.subgraph(Set(idD, idB)) shouldBe FlowGraph(Set(nodeA, nodeB, nodeC, nodeD), Set(edge1, edge2, edge3))
  }

  test("Graph with a cycle allows to calculate a subgraph") {
    val node1 = Node(Node.Id.randomId, op1To1)
    val node2 = Node(Node.Id.randomId, op1To1)
    val cyclicGraph = FlowGraph(
      Set(node1, node2),
      Set(Edge((node1, 0), (node2, 0)), Edge((node2, 0), (node1, 0)))
    )

    cyclicGraph.subgraph(Set(node1.id)) shouldBe cyclicGraph
  }
}
