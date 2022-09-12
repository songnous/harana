package com.harana.sdk.backend.models.flow.json.workflow

import com.harana.sdk.shared.models.designer.flow.{Variables}
import com.harana.sdk.shared.models.designer.flow.flows._
import com.harana.sdk.shared.models.flow.FlowType
import io.circe.Json
import io.circe.syntax.EncoderOps


class WorkflowWithVariablesJsonProtocolSpec extends WorkflowJsonTestSupport {

  "WorkflowWithVariables" should {

    "be serialized to json" in {
      val (workflow, json) = workflowWithVariablesFixture
      workflow.asJson shouldBe json
    }

    "be deserialized from json" in {
      val (workflow, json) = workflowWithVariablesFixture
      json.as[WorkflowWithVariables] shouldBe workflow
    }
  }

  def workflowWithVariablesFixture: (WorkflowWithVariables, Json) = {
    val workflowId = Workflow.Id.randomId

    val workflow = WorkflowWithVariables(
      workflowId,
      FlowType.Batch,
      "0.4.0",
      graph,
      Map("example" -> Seq(Json.fromInt(1), Json.fromInt(2), Json.fromInt(3)).asJson).asJson,
      Variables()
    )

    val workflowJson = Map(
      "id"             -> Json.fromString(workflowId.toString),
      "metadata"       -> Map(
                            "type"       -> Json.fromString("batch"),
                            "apiVersion" -> Json.fromString("0.4.0")
                          ).asJson,
      "workflow"       -> graph.asJson,
      "thirdPartyData" -> Map("example" -> Seq(Json.fromInt(1), Json.fromInt(2), Json.fromInt(3)).asJson).asJson,
      "variables"      -> Json.Null
    )

    (workflow, workflowJson.asJson)
  }
}
