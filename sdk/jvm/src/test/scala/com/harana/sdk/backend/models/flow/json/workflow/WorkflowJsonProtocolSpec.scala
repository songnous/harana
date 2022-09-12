package com.harana.sdk.backend.models.flow.json.workflow

import com.harana.sdk.shared.models.designer.flow.flows.Workflow
import com.harana.sdk.shared.models.flow.FlowType
import io.circe.Json
import io.circe.syntax._

class WorkflowJsonProtocolSpec extends WorkflowJsonTestSupport {

  "Workflow" should {
    "be serialized to json" in {
      val (workflow, json) = workflowFixture
      workflow.asJson shouldBe json
    }

    "be deserialized from json" in {
      val (workflow, json) = workflowFixture
      json.as[Workflow] shouldBe workflow
    }
  }

  def workflowFixture = {
    val workflow = Workflow(
      FlowType.Batch,
      "0.4.0",
      graph,
      ("example" -> Seq(1, 2, 3)).asJson
    )

    val workflowJson = Map(
      "metadata"       -> Map(
                            "type"       -> Json.fromString("batch"),
                            "apiVersion" -> Json.fromString("0.4.0")
                          ).asJson,
      "workflow"       -> graph.asJson,
      "thirdPartyData" -> Map("example" -> Seq(Json.fromInt(1), Json.fromInt(2), Json.fromInt(3))).asJson
    )
    (workflow, workflowJson.asJson)
  }
}
