package com.harana.workflowexecutor.communication.mq.serialization.json

import com.harana.sdk.backend.models.flow.json.StandardSpec
import com.harana.sdk.shared.models.designer.flow.FlowType
import com.harana.sdk.shared.models.designer.flow.flows.Workflow
import com.harana.sdk.shared.models.designer.flow.graph.FlowGraph
import com.harana.workflowexecutor.communication.message.workflow.{Abort, Synchronize, UpdateWorkflow}
import io.circe.Json
import org.scalatestplus.mockito.MockitoSugar

import java.nio.charset.StandardCharsets

class ProtocolJsonDeserializerSpec extends StandardSpec with MockitoSugar {

  "ProtocolJsonDeserializer" should {

    "deserialize Abort messages" in {
      val workflowId = Workflow.Id.randomId

      val rawMessage = Map(
        "messageType" -> Json.fromString("abort"),
        "messageBody" -> Map(
          "workflowId" -> Json.fromString(workflowId.toString)
        )
      )

      val readMessage = serializeAndRead(rawMessage)
      readMessage shouldBe Abort(workflowId)
    }

    "deserialize UpdateWorkflow messages" in {
      val protocolDeserializer = ProtocolJsonDeserializer()
      val workflowId = Workflow.Id.randomId

      val rawMessage = Json(
        "messageType" -> Json.fromString("updateWorkflow"),
        "messageBody" -> Json(
          "workflowId" -> Json.fromString(workflowId.toString),
          "workflow" -> Json(
            "metadata" -> Json(
              "type" -> Json.fromString("batch"),
              "apiVersion" -> Json.fromString("1.0.0")
            ),
            "workflow" -> Json(
              "nodes" -> JArray(),
              "connections" -> JArray()
            ),
            "thirdPartyData" -> Json.Null
          )
        )
      )

      val readMessage = serializeAndRead(rawMessage, protocolDeserializer)
      readMessage shouldBe UpdateWorkflow(
        workflowId,
        Workflow(FlowType.Batch, "1.0.0", FlowGraph(), Json())
      )
    }

    "deserialize Synchronize messages" in {
      val rawMessage = Map("messageType" -> Json.fromString("synchronize"), "messageBody" -> Json())
      serializeAndRead(rawMessage) shouldBe Synchronize()
    }
  }

  private def serializeAndRead(rawMessage: Json, protocolDeserializer: ProtocolJsonDeserializer = ProtocolJsonDeserializer()) = {
    val bytes = rawMessage.compactPrint.getBytes(StandardCharsets.UTF_8)
    protocolDeserializer.deserializeMessage(bytes)
  }

}
