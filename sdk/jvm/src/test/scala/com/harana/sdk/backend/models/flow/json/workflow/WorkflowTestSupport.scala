package com.harana.sdk.backend.models.flow.json.workflow

import com.harana.sdk.backend.models.flow.{Action, Knowledge}
import com.harana.sdk.backend.models.flow.actions.custom.{Sink, Source}
import com.harana.sdk.backend.models.flow.inference.InferenceWarnings
import com.harana.sdk.backend.models.flow.json.{StandardSpec, UnitTestSupport}
import com.harana.sdk.backend.models.flow.inference.InferenceWarnings
import com.harana.sdk.backend.models.flow.{Action, Knowledge}
import com.harana.sdk.shared.models.designer.flow.graph.Endpoint
import com.harana.sdk.shared.models.designer.flow._
import com.harana.sdk.shared.models.flow.{ActionInfo, ActionObjectInfo}
import com.harana.sdk.shared.models.flow.graph.{Edge, FlowGraph}
import com.harana.sdk.shared.models.flow.graph.node.Node
import com.harana.sdk.shared.models.flow.utils.Id
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._

import scala.reflect.runtime.{universe => ru}

trait WorkflowTestSupport extends StandardSpec with UnitTestSupport {

  val catalog = mock[ActionCatalog]

  val actionObject = mockActionObject()

  val actionObjectCatalog = mock[ActionObjectCatalog]
  when(actionObjectCatalog.concreteSubclassesInstances(any(classOf[ru.TypeTag[ActionObjectInfo]]))).thenReturn(Set(actionObject))

  val action1 = mockAction(0, 1, ActionInfo.Id.randomId, "name1", "version1")
  val action2 = mockAction(1, 1, ActionInfo.Id.randomId, "name2", "version2")
  val action3 = mockAction(1, 1, ActionInfo.Id.randomId, "name3", "version3")

  val action4 = mockAction(2, 1, ActionInfo.Id.randomId, "name4", "version4")

  when(catalog.createAction(action1.id)).thenReturn(action1)
  when(catalog.createAction(action2.id)).thenReturn(action2)
  when(catalog.createAction(action3.id)).thenReturn(action3)
  when(catalog.createAction(action4.id)).thenReturn(action4)

  val node1 = Node(Node.Id.randomId, action1)
  val node2 = Node(Node.Id.randomId, action2)
  val node3 = Node(Node.Id.randomId, action3)
  val node4 = Node(Node.Id.randomId, action4)
  val nodes = Set(node1, node2, node3, node4)

  val preEdges = Set((node1, node2, 0, 0), (node1, node3, 0, 0), (node2, node4, 0, 0), (node3, node4, 0, 1))
  val edges = preEdges.map(n => Edge(Endpoint(n._1.id, n._3), Endpoint(n._2.id, n._4)))
  val graph = FlowGraph(nodes, edges)

  val sourceId = new Source().id
  val sourceAction = mockAction(0, 1, sourceId, "Source", "ver1")
  when(catalog.createAction(sourceId)).thenReturn(sourceAction)

  val sinkId = new Sink().id
  val sinkAction = mockAction(1, 0, sinkId, "Sink", "ver1")
  when(catalog.createAction(sinkId)).thenReturn(sinkAction)

  val sourceNode = Node(Node.Id.randomId, sourceAction)
  val sinkNode = Node(Node.Id.randomId, sinkAction)

  val innerWorkflowGraph = FlowGraph(nodes ++ Set(sourceNode, sinkNode), edges)

  def mockAction(inArity: Int, outArity: Int, id: Id, name: String, version: String) = {
    val action = mock[Action]
    when(action.id).thenReturn(id)
    when(action.name).thenReturn(name)
    when(action.inArity).thenReturn(inArity)
    when(action.outArity).thenReturn(outArity)
    when(action.inPortTypes).thenReturn(Vector.fill(inArity)(implicitly[ru.TypeTag[ActionObjectInfo]]))
    when(action.outPortTypes).thenReturn(Vector.fill(outArity)(implicitly[ru.TypeTag[ActionObjectInfo]]))

    val actionObjectMock = mockActionObject()
    val knowledge = mock[Knowledge[ActionObjectInfo]]
    when(knowledge.types).thenReturn(Seq[ActionObjectInfo](actionObjectMock))
    when(knowledge.filterTypes(any())).thenReturn(knowledge)
    when(action.inferKnowledgeUntyped(any())(any())).thenReturn((Vector.fill(outArity)(knowledge), InferenceWarnings.empty))
    when(action.sameAs(isA(classOf[Action]))).thenReturn(true)
    action
  }

  def mockActionObject() = {
    val actionObject = mock[ActionObjectInfo]
    when(actionObject.inferenceResult).thenReturn(None)
    actionObject
  }
}