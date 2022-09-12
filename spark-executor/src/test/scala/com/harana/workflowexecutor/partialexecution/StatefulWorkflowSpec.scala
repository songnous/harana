package com.harana.workflowexecutor.partialexecution

import com.harana.sdk.backend.models.designer.flow.json.workflow.WorkflowTestSupport
import com.harana.sdk.backend.models.designer.flow.workflows.InferredState
import com.harana.sdk.backend.models.designer.flow.{CommonExecutionContext, workflows}
import com.harana.sdk.shared.models.designer.flow.{ActionInfo, EntitiesMap, ExecutionReport, FlowType, NodeState}
import com.harana.sdk.shared.models.designer.flow.flows._
import com.harana.sdk.shared.models.designer.flow.graph.node.Node
import com.harana.sdk.shared.models.designer.flow.graph.node.NodeStatus.{Completed, Draft}
import com.harana.sdk.shared.models.designer.flow.graph.{Edge, Endpoint, FlowGraph}
import com.harana.sdk.shared.models.designer.flow.utils.Id
import io.circe.Json
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar

import java.time.Instant

class StatefulWorkflowSpec extends WorkflowTestSupport with MockitoSugar {

  class LocalData {

    val node1Id = Node.Id.randomId
    val node2Id = Node.Id.randomId
    val node3Id = Node.Id.randomId
    val graph1 = createGraph(node1Id, node2Id)
    val graph2 = createGraph(node1Id, node3Id)
    val originalGraph = FlowGraph()

    val now = Instant.now

    val state1 = NodeState(Completed(now, now, Seq()), Some(EntitiesMap()))
    val state2 = NodeState(Completed(now, now, Seq()), Some(EntitiesMap()))
    val states = Map(node1Id -> workflows.NodeStateWithResults(state1, Map(), None), node2Id -> workflows.NodeStateWithResults(state2, Map(), None))
    val statefulGraph = StatefulGraph(createGraph(node1Id, node2Id), states, None)

    val workflowId = Workflow.Id.randomId
    val workflowWithResults = WorkflowWithResults(workflowId, metadata, originalGraph, Json.Null, ExecutionReport(Map()), WorkflowInfo.empty())
    val workflow = Workflow(FlowType.Batch, "1.0.0", mock[FlowGraph], mock[Json])
    val successfulExecution = IdleExecution(statefulGraph)

    val statefulWorkflow = new StatefulWorkflow(
      mock[CommonExecutionContext],
      workflowId,
      FlowType.Batch,
      "1.0.0",
      WorkflowInfo.forId(workflowId),
      Json.Null,
      successfulExecution,
      new MockStateInferrer()
    )

    def createGraph(node1Id: Node.Id, node2Id: Node.Id): FlowGraph = {
      val node1 = Node(node1Id, mockAction(0, 1, ActionInfo.Id.randomId, "a", "b"))
      val node2 = Node(node2Id, mockAction(1, 0, ActionInfo.Id.randomId, "c", "d"))
      FlowGraph(Set(node1, node2), Set(Edge(Endpoint(node1.id, 0), Endpoint(node2.id, 0))))
    }
  }

  object ExecutionReportData {
    val nodeId: Id = Node.Id.randomId
    def apply(): ExecutionReport = ExecutionReport(Map(nodeId -> NodeState(Draft(), Some(EntitiesMap()))))
  }

  "StatefulWorkflow" should {
    val workflow = Workflow(FlowType.Batch, "1.0.0", mock[FlowGraph], mock[Json])
    val executionReport = ExecutionReportData()

    "actually updateStruct only" when {

      "execution is idle (but third party should be updated anyway)" in {
        val ld = new LocalData
        val updatedExecution = mock[IdleExecution]
        when(updatedExecution.executionReport).thenReturn(executionReport)

        def idleExecutionFactory(graph: StatefulGraph) = {
          val exec = mock[IdleExecution]
          when(exec.updateStructure(workflow.graph)).thenReturn(updatedExecution)
          exec
        }

        val statefulWorkflow = StatefulWorkflow(mock[CommonExecutionContext], ld.workflowWithResults, idleExecutionFactory)
        statefulWorkflow.updateStructure(workflow)
        statefulWorkflow.currentExecution shouldBe updatedExecution
        statefulWorkflow.currentAdditionalData shouldBe workflow.additionalData
      }
    }

    "ignore struct update and only update thirdPartyData when execution is started" in {
      val ld = new LocalData
      val runningExecution = mock[RunningExecution]
      def runningExecutionFactory(graph: StatefulGraph): Execution = runningExecution
      when(runningExecution.executionReport).thenReturn(executionReport)
      val statefulWorkflow = StatefulWorkflow(mock[CommonExecutionContext], ld.workflowWithResults, runningExecutionFactory)

      statefulWorkflow.updateStructure(workflow)
      statefulWorkflow.currentExecution shouldBe runningExecution             // not changed
      statefulWorkflow.currentAdditionalData shouldBe workflow.additionalData // updated
    }

    "not change states when only thirdPartyData is updated" in {
      val ld = new LocalData
      ld.statefulWorkflow.updateStructure(Workflow(ld.metadata, ld.graph1, Map("foo" -> Json.fromString("bar"))))
      val newStates = ld.statefulWorkflow.executionReport.states
      newStates(ld.node1Id) shouldBe ld.state1
      newStates(ld.node2Id) shouldBe ld.state2
    }

    "saved 0 nodes removed" when {

      "removed 0 nodes" in {
        val ld = new LocalData
        val nodes = ld.statefulWorkflow.getNodesRemovedByWorkflow(Workflow(ld.metadata, ld.graph1, mock[Json]))
        nodes.size shouldBe 0
      }
    }

    "saved 1 node removed" when {

      "removed 1 node" in {
        val ld = new LocalData
        val nodes = ld.statefulWorkflow.getNodesRemovedByWorkflow(Workflow(ld.metadata, ld.graph2, mock[Json]))
        nodes.size shouldBe 1
        nodes.head.id shouldBe ld.node2Id
      }
    }
  }

  private class MockStateInferrer extends StateInferrer {
    override def inferState(execution: Execution): InferredState = mock[InferredState]
  }
}