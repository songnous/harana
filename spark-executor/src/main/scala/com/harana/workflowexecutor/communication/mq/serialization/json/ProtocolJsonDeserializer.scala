package com.harana.workflowexecutor.communication.mq.serialization.json

case class ProtocolJsonDeserializer extends JsonMQDeserializer(
      Seq(
        WorkflowProtocol.AbortDeserializer,
        WorkflowProtocol.UpdateWorkflowDeserializer,
        WorkflowProtocol.SynchronizeDeserializer,
        NotebookProtocol.KernelManagerReadyDeserializer
      )
    )
