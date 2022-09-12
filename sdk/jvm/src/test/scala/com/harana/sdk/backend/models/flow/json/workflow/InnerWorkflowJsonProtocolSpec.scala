package com.harana.sdk.backend.models.flow.json.workflow

import com.harana.sdk.shared.models.flow.graph.node.Node
import com.harana.sdk.shared.models.flow.parameters.custom.{InnerWorkflow, PublicParameter}
import io.circe.Json
import io.circe.syntax.EncoderOps


class InnerWorkflowJsonProtocolSpec extends WorkflowTestSupport {

  val nodeId = Node.Id.randomId

  "InnerWorkflow" should {
    "be serialized to json" in {
      val (innerWorkflow, json) = innerWorkflowFixture
      innerWorkflow.asJson shouldBe json
    }

    "be deserialized from json" in {
      val (innerWorkflow, json) = innerWorkflowFixture
      json.as[InnerWorkflow] shouldBe innerWorkflow
    }
  }

  def innerWorkflowFixture: (InnerWorkflow, Json) = {
    val innerWorkflow = InnerWorkflow(
      innerWorkflowGraph,
      Map("example" -> Seq(Json.fromInt(1), Json.fromInt(2), Json.fromInt(3))).asJson,
      List(PublicParameter(nodeId, "name", "public"))
    )
    val innerWorkflowJson = Map(
      "workflow"           -> innerWorkflowGraph.asJson,
      "thirdPartyData"     -> Map("example" -> Seq(Json.fromInt(1), Json.fromInt(2), Json.fromInt(3)).asJson).asJson,
      "publicParameters"   -> Seq(
                                Map(
                                  "nodeId"          -> Json.fromString(nodeId.toString),
                                  "parameterName"   -> Json.fromString("name"),
                                  "publicName"      -> Json.fromString("public")
                                )
                              ).asJson
    )
    (innerWorkflow, innerWorkflowJson.asJson)
  }
}
