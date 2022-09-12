package com.harana.workflowexecutor.communication.mq.serialization.json

import com.harana.workflowexecutor.communication.message.workflow._
import io.circe.Json

object WorkflowProtocol {
  val abort = "abort"
  val launch = "launch"
  val updateWorkflow = "updateWorkflow"
  val synchronize = "synchronize"

  object AbortDeserializer extends DefaultJsonMessageDeserializer[Abort](abort)
  object SynchronizeDeserializer extends DefaultJsonMessageDeserializer[Synchronize](synchronize)
  object SynchronizeSerializer extends DefaultJsonMessageSerializer[Synchronize](synchronize)

  case class UpdateWorkflowDeserializer extends JsonMessageDeserializer with UpdateWorkflowJsonProtocol {
    private val defaultDeserializer = new DefaultJsonMessageDeserializer[UpdateWorkflow](updateWorkflow)
    override def deserialize: PartialFunction[(String, Json), Any] = defaultDeserializer.deserialize
  }
}d