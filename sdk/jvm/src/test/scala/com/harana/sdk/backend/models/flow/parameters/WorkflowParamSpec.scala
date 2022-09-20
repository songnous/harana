package com.harana.sdk.backend.models.flow.parameters

import com.harana.sdk.backend.models.flow.actions.custom.Sink
import com.harana.sdk.backend.models.flow.actions.custom.Source
import com.harana.sdk.shared.models.flow.parameters.WorkflowParameter
import com.harana.sdk.shared.models.flow.parameters.custom.InnerWorkflow
import io.circe.Json
import io.circe.syntax.EncoderOps

class WorkflowParamSpec extends AbstractParameterSpec[InnerWorkflow, WorkflowParameter] {

  def className = "WorkflowParameter"

  def paramFixture: (WorkflowParameter, Json) = {
      val param = WorkflowParameter("Workflow parameter name", Some(description))
    val expectedJson = Map(
                          "type" -> Json.fromString("workflow"),
                          "name" -> Json.fromString(param.name),
                          "description" -> Json.fromString(description),
                          "isGriddable" -> Json.False,
                          "default" -> Json.Null
                        )
    (param, expectedJson.asJson)
  }

  def valueFixture: (InnerWorkflow, Json) = {
    val innerWorkflow = InnerWorkflow.empty
    val sourceNode = Map(
                        "id"          -> Json.fromString(innerWorkflow.source.id.toString),
                        "action"      -> Map(
                                            "id"    -> Json.fromString(new Source().id),
                                            "name"  -> Json.fromString("Source")
                                          ).asJson,
                        "parameters"  -> Json.Null
                      )
    val sinkNode = Map(
                      "id"          -> Json.fromString(innerWorkflow.sink.id.toString),
                      "action"      -> Map(
                                          "id"    -> new Sink().id.toString,
                                          "name" -> "Sink"
                                        ).asJson,
                      "parameters"  -> Json.Null
                    )
    val workflow = Map(
                      "nodes"       -> Seq(sourceNode.asJson, sinkNode.asJson).asJson,
                      "connections" -> Json.Null
                    )
    val value = Map(
                  "workflow"          -> workflow.asJson,
                  "thirdPartyData"    -> Json.Null,
                  "publicParameters"  -> Json.Null
                )
    (innerWorkflow, value.asJson)
  }
}
