package com.harana.workflowexecutor

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.testkit.TestActorRef
import akka.testkit.TestKit
import akka.testkit.TestProbe
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers.{any, same}
import org.scalatest.concurrent.Eventually
import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.BeforeAndAfter
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import com.harana.sdk.backend.models.flow._
import com.harana.sdk.backend.models.flow.graph.FlowGraph.FlowNode
import com.harana.sdk.backend.models.flow.inference.InferenceWarnings
import com.harana.sdk.shared.models.designer.flow.actionobjects.report.Report
import com.harana.sdk.shared.models.designer.flow.graph.node.Node
import com.harana.sdk.shared.models.designer.flow.report.ReportContent
import com.harana.sdk.shared.models.designer.flow.ActionObjectInfo
import com.harana.spark.AkkaUtils
import com.harana.workflowexecutor.WorkflowExecutorActor.Messages.NodeCompleted
import com.harana.workflowexecutor.WorkflowExecutorActor.Messages.NodeFailed
import com.harana.workflowexecutor.WorkflowExecutorActor.Messages.NodeStarted
import com.harana.workflowexecutor.WorkflowNodeExecutorActor.Messages.Delete
import com.harana.workflowexecutor.WorkflowNodeExecutorActor.Messages.Start

class WorkflowNodeExecutorActorSpec
    extends TestKit(ActorSystem("WorkflowNodeExecutorActorSpec"))
    with AnyWordSpecLike
    with Matchers
    with MockitoSugar
    with BeforeAndAfter
    with BeforeAndAfterAll
    with Eventually {

  override protected def afterAll(): Unit = AkkaUtils.terminate(system)

  "WorkflowNodeExecutorActor" when {
    "receives start" should {
      "infer knowledge and start execution of a node with correct parameters" in {
        val (probe, testedActor, node, action, input) = fixture()
        probe.send(testedActor, Start())
        probe.expectMsg(NodeStarted(node.id))

        eventually {
          verify(action).inferKnowledgeUntyped(any())(any())
          verify(action).executeUntyped(same(input))(any())
        }
      }
    }
    "its action report type is set to metadata" should {
      
      "generate metadata report" in {
        val (probe, testedActor, node, result) = fixtureSucceedingAction()
        node.value.setReportType(Action.ReportParameter.Metadata())
        probe.send(testedActor, Start())
        probe.expectMsg(NodeStarted(node.id))
        val _ = probe.expectMsgType[NodeCompleted]
        verify(result(0), times(1)).report(extended = false)
        verify(result(1), times(1)).report(extended = false)
      }
    }
    
    "receives delete" should {
      
      "use delete DataFrame from storage" in {

        val node = mock[FlowNode]
        when(node.id).thenReturn(Node.Id.randomId)
        val action = mockAction
        when(node.value).thenReturn(action)

        val removedExecutedContext = mock[ExecutionContext]
        val dataFrameStorage = mock[ContextualDataFrameStorage]
        when(removedExecutedContext.dataFrameStorage).thenReturn(dataFrameStorage)

        val wnea = TestActorRef(new WorkflowNodeExecutorActor(removedExecutedContext, node, Vector.empty))
        val probe = TestProbe()
        probe.send(wnea, Delete())

        eventually {
          verify(removedExecutedContext).dataFrameStorage
          verify(dataFrameStorage).removeNodeOutputDataFrames()
          verify(dataFrameStorage).removeNodeInputDataFrames()
        }
      }
    }
    "node completed" should {
      
      "respond NodeCompleted" in {
        val (probe, testedActor, node, output) = fixtureSucceedingAction()
        probe.send(testedActor, Start())
        probe.expectMsg(NodeStarted(node.id))
        val nodeCompleted = probe.expectMsgType[NodeCompleted]
        nodeCompleted.id shouldBe node.id
        nodeCompleted.results.actionObjects.values should contain theSameElementsAs output
      }
    }
    "respond NodeFailed" when {
      
      "node failed" in {
        val (probe, testedActor, node, cause) = fixtureFailingAction()
        probe.send(testedActor, Start())
        probe.expectMsg(NodeStarted(node.id))
        probe.expectMsgType[NodeFailed] shouldBe NodeFailed(node.id, cause)
      }
      
      "node failed with an Error" in {
        val (probe, testedActor, node, cause) = fixtureFailingActionError()
        probe.send(testedActor, Start())
        probe.expectMsg(NodeStarted(node.id))
        val nodeFailed = probe.expectMsgType[NodeFailed]
        nodeFailed shouldBe a[NodeFailed]
        nodeFailed.id shouldBe node.id
        nodeFailed.cause.getCause shouldBe cause
      }
      
      "node's inference throws an exception" in {
        val (probe, testedActor, node, cause) = fixtureFailingInference()
        probe.send(testedActor, Start())
        probe.expectMsg(NodeStarted(node.id))
        probe.expectMsgType[NodeFailed] shouldBe NodeFailed(node.id, cause)
      }
    }
  }

  private def nodeExecutorActor(input: Vector[ActionObjectInfo], node: FlowNode) =
    system.actorOf(Props(new WorkflowNodeExecutorActor(executionContext, node, input)))

  private def inferableActionObject = {
    mock[ActionObjectInfo]
  }

  private def actionObjectWithReports = {
    val actionObject = mock[ActionObjectInfo]
    val report = mock[Report]
    when(report.content).thenReturn(mock[ReportContent])
    when(actionObject.report(extended = false)).thenReturn(report)
    actionObject
  }

  private def mockAction = {
    val action = mock[Action]
    when(action.name).thenReturn("mockedName")
    action
  }

  private def fixtureFailingInference() = {
    val action = mockAction
    val cause = new NullPointerException("test exception")
    when(action.inferKnowledgeUntyped(any())(any())).thenThrow(cause)
    val (probe, testedActor, node, _, _) = fixtureWithAction(action)
    (probe, testedActor, node, cause)
  }

  private def fixtureFailingAction() = {
    val action = mockAction
    val cause = new NullPointerException("test exception")
    when(action.executeUntyped(any[Vector[ActionObjectInfo]]())(any[ExecutionContext]())).thenThrow(cause)
    val (probe, testedActor, node, _, _) = fixtureWithAction(action)
    (probe, testedActor, node, cause)
  }

  private def fixtureFailingActionError() = {
    val action = mockAction
    val cause = new AssertionError("test exception")
    when(action.executeUntyped(any[Vector[ActionObjectInfo]]())(any[ExecutionContext]())).thenThrow(cause)
    val (probe, testedActor, node, _, _) = fixtureWithAction(action)
    (probe, testedActor, node, cause)
  }

  private def fixtureSucceedingAction() = {
    val action = mockAction
    val output = Vector(actionObjectWithReports, actionObjectWithReports)
    when(action.executeUntyped(any())(any())).thenReturn(output)
    val (probe, testedActor, node, _, _) = fixtureWithAction(action)
    (probe, testedActor, node, output)
  }

  private def fixtureWithAction(action: Action) = {
    val node = mock[FlowNode]
    when(node.id).thenReturn(Node.Id.randomId)
    when(node.value).thenReturn(action)
    val probe = TestProbe()
    val input = Vector(inferableActionObject, inferableActionObject)
    val testedActor = nodeExecutorActor(input, node)
    (probe, testedActor, node, action, input)
  }

  private def fixture() = {
    val action = mockAction
    when(action.inferKnowledgeUntyped(any())(any())).thenReturn((Vector[Knowledge[ActionObjectInfo]](), mock[InferenceWarnings]))
    when(action.executeUntyped(any())(any())).thenReturn(Vector())
    fixtureWithAction(action)
  }

  val executionContext = LocalExecutionContext.createExecutionContext()
}