package com.harana.sdk.backend.models.flow.json.workflow

import com.harana.sdk.shared.models.flow.graph.node.NodeStatus.Completed
import com.harana.sdk.shared.models.designer.flow.flows._
import com.harana.sdk.shared.models.flow.{EntitiesMap, ExecutionReport, FlowType}
import com.harana.sdk.shared.models.flow.utils.Entity
import io.circe.Json
import io.circe.syntax.EncoderOps

import java.util.UUID

class WorkflowWithResultsJsonProtocolSpec extends WorkflowJsonTestSupport {

  "WorkflowWithResults" should {

    "be serialized to json" in {
      val (workflow, json) = workflowWithResultsFixture
      workflow.asJson shouldBe json
    }

    "be deserialized from json" in {
      val (workflow, json) = workflowWithResultsFixture
      json.as[WorkflowWithResults] shouldBe workflow
    }
  }

  private def workflowWithResultsFixture = {

    val (executionReport, executionReportJson) = executionReportFixture
    val (workflowInfo, workflowInfoJson) = workflowInfoFixture

    val workflowId = Workflow.Id.randomId

    val workflow = WorkflowWithResults(
      workflowId,
      FlowType.Batch,
      "0.4.0",
      graph,
      Map("example" -> Seq(Json.fromInt(1), Json.fromInt(2), Json.fromInt(3))).asJson,
      executionReport,
      workflowInfo
    )

    val workflowJson = Map(
      "id"              -> Json.fromString(workflowId.toString),
      "metadata"        -> Map(
                              "type"       -> Json.fromString("batch"),
                              "apiVersion" -> Json.fromString("0.4.0")
                            ).asJson,
      "workflow"        -> graph.asJson,
      "thirdPartyData"  -> Map("example" -> Seq(Json.fromInt(1), Json.fromInt(2), Json.fromInt(3))).asJson,
      "executionReport" -> executionReportJson,
      "workflowInfo"    -> workflowInfoJson
    )

    (workflow, workflowJson.asJson)
  }

  private def executionReportFixture: (ExecutionReport, Json) = {

    val startDateTime   = dateTime(2015, 5, 12, 21, 11, 9)
    val finishDateTime  = dateTime(2015, 5, 12, 21, 12, 50)
    val startTimestamp  = dateToString(startDateTime)
    val finishTimestamp = dateToString(finishDateTime)

    val entity1Id = Entity.Id.randomId
    val entity2Id = Entity.Id.randomId

    val executionReport = ExecutionReport(
      Map(
        node1.id -> Completed(startDateTime, finishDateTime, Seq(entity1Id, entity2Id))
      ),
      EntitiesMap(),
      None
    )

    val executionReportJson = Map(
      "error"          -> Json.Null,
      "nodes"          -> Map(
                            node1.id.toString -> Map(
                                                    "status"  -> Json.fromString("COMPLETED"),
                                                    "started" -> Json.fromString(startTimestamp),
                                                    "ended"   -> Json.fromString(finishTimestamp),
                                                    "results" -> Seq(entity1Id.toString, entity2Id.toString).asJson,
                                                    "error"   -> Json.Null)
                          ).asJson,
      "resultEntities" -> Json.Null
    )

    (executionReport, executionReportJson.asJson)
  }

  private def workflowInfoFixture: (WorkflowInfo, Json) = {

    val createdDateTime  = dateTime(2015, 5, 12, 21, 11, 9)
    val updatedDateTime  = dateTime(2015, 5, 12, 21, 12, 50)
    val createdTimestamp = dateToString(createdDateTime)
    val updatedTimestamp = dateToString(updatedDateTime)

    val workflowId = Workflow.Id.randomId
    val workflowName = " workflow name "
    val description = " some description "
    val ownerId = UUID.randomUUID.toString
    val ownerName = "some@email.com"
    val workflowInfo = WorkflowInfo(workflowId, workflowName, description, createdDateTime, updatedDateTime, ownerId, ownerName)

    val workflowInfoJson = Map(
      "id"          -> workflowId.toString,
      "name"        -> workflowName,
      "description" -> description,
      "created"     -> createdTimestamp,
      "updated"     -> updatedTimestamp,
      "ownerId"     -> ownerId,
      "ownerName"   -> ownerName
    )

    (workflowInfo, workflowInfoJson.asJson)
  }
}
