package com.harana.sdk.backend.models.flow.models.workflows

import com.harana.sdk.backend.models.flow.{Knowledge, graph}
import com.harana.sdk.backend.models.flow.graph.NodeInferenceResult
import com.harana.sdk.backend.models.flow.inference.InferenceWarnings
import com.harana.sdk.backend.models.flow.workflows.NodeStateWithResults
import com.harana.sdk.backend.models.flow.inference.InferenceWarnings
import com.harana.sdk.backend.models.flow.workflows.NodeStateWithResults
import com.harana.sdk.shared.models.designer.flow
import com.harana.sdk.shared.models.flow.ActionObjectInfo
import com.harana.sdk.shared.models.flow.exceptions.FlowError
import com.harana.sdk.shared.models.flow.report.ReportContent
import com.harana.sdk.shared.models.flow.utils.Entity
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar

class NodeStateWithResultsSpec extends AnyWordSpec with Matchers with MockitoSugar {

  "NodeStateWithResults" should {

    "copy knowledge, keep warnings and clear errors for nonempty ActionObject list" in {
      val draftNode = draftNodeState
      val (entityIds, actionObjects, reportsMap, actionObjectsMap) = executionResultFixture(2)
      val finished = draftNode.enqueue.start.finish(entityIds, reportsMap, actionObjectsMap)

      finished.nodeState.isCompleted shouldBe true
      finished.knowledge shouldBe Some(
        graph.NodeInferenceResult(actionObjects.map(Knowledge(_)).toVector, draftNode.knowledge.get.warnings, Vector())
      )
    }

    "copy knowledge, keep warnings and clear errors for empty ActionObject list" in {
      val draftNode = draftNodeState
      val (entityIds, actionObjects, reportsMap, actionObjectsMap) = executionResultFixture(0)
      val finished = draftNode.enqueue.start.finish(entityIds, reportsMap, actionObjectsMap)

      finished.nodeState.isCompleted shouldBe true
      finished.knowledge shouldBe Some(graph.NodeInferenceResult(Vector(), draftNode.knowledge.get.warnings, Vector()))
    }
  }

  private def draftNodeState = {
    NodeStateWithResults.draft.withKnowledge(
      NodeInferenceResult(Vector(Knowledge(mock[ActionObjectInfo])), mock[InferenceWarnings], Vector(mock[FlowError]))
    )
  }

  private def executionResultFixture(actionObjectCount: Int) = {
    val entityIds = (1 to actionObjectCount).map(_ => Entity.Id.randomId).toList
    val actionObjects = entityIds.map(_ => mock[ActionObjectInfo])
    val reportsMap = entityIds.map(id => id -> mock[ReportContent]).toMap
    val actionObjectsMap = entityIds.zip(actionObjects).toMap
    (entityIds, actionObjects, reportsMap, actionObjectsMap)
  }
}
