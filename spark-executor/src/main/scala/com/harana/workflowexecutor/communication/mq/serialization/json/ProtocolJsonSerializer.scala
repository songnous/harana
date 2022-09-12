package com.harana.workflowexecutor.communication.mq.serialization.json

case class ProtocolJsonSerializer extends JsonMQSerializer(Seq(WorkflowProtocol.SynchronizeSerializer))
