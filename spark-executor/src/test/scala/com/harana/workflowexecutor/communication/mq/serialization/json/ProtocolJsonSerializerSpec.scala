package com.harana.workflowexecutor.communication.mq.serialization.json

import com.harana.sdk.backend.models.designer.flow.json.StandardSpec
import com.harana.workflowexecutor.communication.message.workflow.Synchronize
import org.scalatestplus.mockito.MockitoSugar

import java.nio.charset.Charset

class ProtocolJsonSerializerSpec
    extends StandardSpec
    with MockitoSugar
    with WorkflowWithResultsJsonProtocol
    with InferredStateJsonProtocol
    with HeartbeatJsonProtocol {

  "ProtocolJsonSerializer" should {
    val protocolJsonSerializer = ProtocolJsonSerializer

    "serialize Synchronize messages" in {
      protocolJsonSerializer.serializeMessage(Synchronize()) shouldBe
        expectedSerializationResult("synchronize", Json())
    }
  }

  private def expectedSerializationResult(messageType: String, jsonObject: Json) = {
    Map(
      "messageType" -> Json.fromString(messageType),
      "messageBody" -> jsonObject
    ).compactPrint.getBytes(Charset.forName("UTF-8"))
  }
}
