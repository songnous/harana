package com.harana.workflowexecutor.communication.mq.serialization.json

import com.harana.sdk.backend.models.designer.flow.workflows.InferredState
import com.harana.sdk.shared.models.designer.flow.ExecutionReport
import java.nio.charset.Charset
import com.harana.workflowexecutor.communication.mq.serialization.json.Constants.MessagesTypes._
import com.harana.workflowexecutor.communication.protocol.{Heartbeat, Launch, PoisonPill, Ready}

object Global {
  val charset = Charset.forName("UTF-8")

  val inferredStateJsonProtocol = InferredStateJsonProtocol
  import inferredStateJsonProtocol._

  object HeartbeatDeserializer extends DefaultJsonMessageDeserializer[Heartbeat](heartbeat)
  object HeartbeatSerializer extends DefaultJsonMessageSerializer[Heartbeat](heartbeat)

  object PoisonPillDeserializer extends DefaultJsonMessageDeserializer[PoisonPill](poisonPill)
  object PoisonPillSerializer extends DefaultJsonMessageSerializer[PoisonPill](poisonPill)

  object ReadyDeserializer extends DefaultJsonMessageDeserializer[Ready](ready)
  object ReadySerializer extends DefaultJsonMessageSerializer[Ready](ready)

  object LaunchDeserializer extends DefaultJsonMessageDeserializer[Launch](launch)
  object LaunchSerializer extends DefaultJsonMessageSerializer[Launch](launch)

  object ExecutionReportSerializer extends DefaultJsonMessageSerializer[ExecutionReport](executionReport)
  object ExecutionReportDeserializer extends DefaultJsonMessageDeserializer[ExecutionReport](executionReport)

  object InferredStateSerializer extends DefaultJsonMessageSerializer[InferredState](inferredState)
  object InferredStateDeserializer extends DefaultJsonMessageDeserializer[InferredState](inferredState)

  object GlobalMQSerializer extends JsonMQSerializer(
    Seq(HeartbeatSerializer,
      PoisonPillSerializer,
      ReadySerializer,
      LaunchSerializer,
      ExecutionReportSerializer,
      InferredStateSerializer
    ))

  object GlobalMQDeserializer extends JsonMQDeserializer(
    Seq(HeartbeatDeserializer,
      PoisonPillDeserializer,
      ReadyDeserializer,
      LaunchDeserializer,
      ExecutionReportDeserializer,
      InferredStateDeserializer
    ))
}
