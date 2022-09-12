package com.harana.workflowexecutor.communication.mq.serialization.json

import io.circe.Json

trait JsonMessageSerializer {
  def serialize: PartialFunction[Any, Json]
}