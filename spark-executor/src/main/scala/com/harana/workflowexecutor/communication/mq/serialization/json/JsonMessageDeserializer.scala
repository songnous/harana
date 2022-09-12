package com.harana.workflowexecutor.communication.mq.serialization.json

import io.circe.Json

trait JsonMessageDeserializer {
  def deserialize: PartialFunction[(String, Json), Any]
}
