package com.harana.workflowexecutor.communication.mq.serialization.json

trait MQSerializer {
  def serializeMessage(message: Any): Array[Byte]
}
