package com.harana.sdk.backend.models.flow.json.workflow

import com.harana.sdk.backend.models.flow.Knowledge
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.graph.{GraphKnowledge, NodeInferenceResult}
import com.harana.sdk.backend.models.flow.inference.{InferenceWarning, InferenceWarnings}
import com.harana.sdk.backend.models.flow.workflows.InferredState
import com.harana.sdk.shared.models.designer.flow.flows.Workflow
import com.harana.sdk.shared.models.designer.flow.{graph}
import com.harana.sdk.shared.models.designer.flow.json.workflow.InferredStateJsonProtocol
import com.harana.sdk.shared.models.flow.{ActionObjectInfo, ExecutionReport}
import com.harana.sdk.shared.models.flow.actionobjects.descriptions.{DataFrameInferenceResult, ParametersInferenceResult}
import com.harana.sdk.shared.models.flow.exceptions.FlowError
import com.harana.sdk.shared.models.flow.graph.node.NodeStatus
import com.harana.sdk.shared.models.flow.parameters.Parameters
import com.harana.sdk.shared.models.flow.utils.Entity
import io.circe.Json
import io.circe.syntax.EncoderOps
import org.apache.spark.sql.types._
import org.joda.time.DateTime
import org.mockito.Mockito._

import java.time.Instant

class InferredStateJsonProtocolSpec extends WorkflowJsonTestSupport {

  "InferredState" should {

    "be serializable to json" in {
      val (inferredState, json) = inferredStateFixture
      inferredState.asJson shouldBe json
    }
  }

  def inferredStateFixture = {
    val workflowId = Workflow.Id.randomId
    val (graphKnowledge, graphKnowledgeJson) = graphKnowledgeFixture
    val (executionStates, statesJson) = executionStatesFixture

    val workflow = InferredState(workflowId, graphKnowledge, executionStates)

    val workflowJson = Map(
                          "id"        -> Json.fromString(workflowId.toString),
                          "knowledge" -> graphKnowledgeJson,
                          "states"    -> statesJson
                        )
    (workflow, workflowJson.asJson)
  }

  def graphKnowledgeFixture: (GraphKnowledge, Json) = {
    val parametricActionObject = mock[ParametricActionObjectInfo]("ParametricActionObject")
    val paramSchema = Json.fromString("Js with ParamSchema")
    val parameterValues = Json.fromString("Js with ParamValues")
    when(parametricActionObject.inferenceResult).thenReturn(
      Some(ParametersInferenceResult(paramSchema, parameterValues))
    )

    val dataFrame = mock[DataFrame]
    val meta = new MetadataBuilder().putString("someKey", "someValue").build()
    val dataFrameDescription = DataFrameInferenceResult(
      StructType(
        Seq(
          StructField("col1", StringType, nullable = true),
          StructField("col2", DoubleType, nullable = false, metadata = meta)
        )
      )
    )
    when(dataFrame.inferenceResult).thenReturn(Some(dataFrameDescription))

    val graphKnowledge = GraphKnowledge().addInference(
      node1.id,
      NodeInferenceResult(
        List(
          Knowledge(Set(actionObject)),
          Knowledge(Set(actionObject, parametricActionObject)),
          Knowledge(Set[ActionObjectInfo](parametricActionObject)),
          Knowledge(Set[ActionObjectInfo](dataFrame))
        ),
        InferenceWarnings(new InferenceWarning("warning1") {}, new InferenceWarning("warning2") {}),
        List(
          new FlowError("error1") {},
          new FlowError("error2") {}
        )
      )
    )

    def actionObjectJsName(o: ActionObjectInfo) = Json.fromString(o.getClass.getCanonicalName)
    val mockActionObjectName = actionObjectJsName(actionObject)
    val parametricActionObjectName = actionObjectJsName(parametricActionObject)
    val dataFrameName = actionObjectJsName(dataFrame)

    val knowledgeJson = Map(
      node1.id.toString -> Map(
        "ports"    -> Seq(
                        Map(
                          "types"  -> Seq(mockActionObjectName).asJson,
                          "result" -> Json.Null
                        ),
                        Map(
                          "types"  -> Seq(mockActionObjectName, parametricActionObjectName).asJson,
                          "result" -> Json.Null
                        ),
                        Map(
                          "types"  -> Seq(parametricActionObjectName).asJson,
                          "result" -> Map(
                            "parameters" -> Map(
                                              "schema" -> paramSchema,
                                              "values" -> parameterValues
                                            )
                          ).asJson
                        ),
                        Map(
                          "types"  -> Seq(dataFrameName).asJson,
                          "result" -> Map(
                            "schema" -> Map(
                              "fields" -> Seq(
                                            Map(
                                              "name"      -> Json.fromString("col1"),
                                              "dataType"  -> Json.fromString("string"),
                                              "flowType"  -> Json.fromString("string"),
                                              "nullable"  -> Json.True
                                            ),
                                            Map(
                                              "name"      -> Json.fromString("col2"),
                                              "dataType"  -> Json.fromString("double"),
                                              "flowType"  -> Json.fromString("numeric"),
                                              "nullable"  -> Json.False
                                            )
                                          ).asJson
                            )
                          ).asJson
                        )
                      ).asJson,
        "warnings" -> Seq(Json.fromString("warning1"), Json.fromString("warning2")).asJson,
        "errors"   -> Seq(Json.fromString("error1"), Json.fromString("error2")).asJson
      )
    )

    (graphKnowledge, knowledgeJson.asJson)
  }

  def executionStatesFixture = {

    val startTimestamp  = "2015-05-12T21:11:09.000Z"
    val finishTimestamp = "2015-05-12T21:12:50.000Z"

    val entity1Id = Entity.Id.randomId
    val entity2Id = Entity.Id.randomId

    val executionStates = ExecutionReport.statesOnly(
      Map(
        node1.id -> NodeStatus.Completed(
          Instant.parse(startTimestamp),
          Instant.parse(finishTimestamp),
          Seq(entity1Id, entity2Id)
        )
      ),
      None
    )
    val executionStatesJson = Map(
      "error"          -> Json.Null,
      "nodes"          -> Map(
                            node1.id.toString -> Map(
                              "status"  -> Json.fromString("COMPLETED"),
                              "started" -> Json.fromString(startTimestamp),
                              "ended"   -> Json.fromString(finishTimestamp),
                              "results" -> Seq(
                                              Json.fromString(entity1Id.toString),
                                              Json.fromString(entity2Id.toString)
                                            ).asJson,
                              "error"   -> Json.Null
                            )
                          ).asJson,
      "resultEntities" -> Json.Null
    )

    (executionStates, executionStatesJson.asJson)
  }

  abstract class ParametricActionObjectInfo extends ActionObjectInfo with Parameters

}
