package com.harana.workflowexecutor.communication.mq.serialization.json

import io.circe.Json

class DefaultJsonMessageDeserializer[T : JsonReader](handledName: String) extends JsonMessageDeserializer {

  val deserialize: PartialFunction[(String, Json), Any] = {
    case (name, body) if isHandled(name) => handle(body)
  }

  private def isHandled(name: String): Boolean = name == handledName
  private def handle(body: Json): Any = body.as[T]

}