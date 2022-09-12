package com.harana.workflowexecutor.communication.mq.serialization.json

import io.circe.Json

import java.nio.charset.Charset

class JsonMQSerializer(jsonSerializers: Seq[JsonMessageSerializer], parent: Option[JsonMQSerializer] = None) extends MQSerializer with JsonMessageSerializer {

  private val combinedJsonSerializers = {
    jsonSerializers.tail.foldLeft(jsonSerializers.head.serialize) {
      case (acc, serializer) =>
        acc.orElse(serializer.serialize)
    }
  }

  override val serialize: PartialFunction[Any, Json] = {
    parent match {
      case Some(p) => combinedJsonSerializers.orElse(p.serialize)
      case None => combinedJsonSerializers
    }
  }

  override def serializeMessage(message: Any) =
    serialize(message).compactPrint.getBytes(Charset.forName("UTF-8"))

  def orElse(next: JsonMQSerializer) =
    new JsonMQSerializer(jsonSerializers, Some(next))
}