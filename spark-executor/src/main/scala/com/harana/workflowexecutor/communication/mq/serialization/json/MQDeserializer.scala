package com.harana.workflowexecutor.communication.mq.serialization.json

trait MQDeserializer {
  def deserializeMessage(data: Array[Byte]): Any
}
