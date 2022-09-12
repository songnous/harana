package com.harana.workflowexecutor.communication.mq.serialization.json



import java.nio.charset.Charset

class JsonMQDeserializer(jsonDeserializers: Seq[JsonMessageDeserializer], parent: Option[JsonMQDeserializer] = None) extends MQDeserializer with JsonMessageDeserializer {

  private val combinedJsonDeserializers = {
    jsonDeserializers.tail.foldLeft(jsonDeserializers.head.deserialize) {
      case (acc, deserializer) =>
        acc.orElse(deserializer.deserialize)
    }
  }

  override val deserialize: PartialFunction[(String, Json), Any] = {
    parent match {
      case Some(p) => combinedJsonDeserializers.orElse(p.deserialize)
      case None => combinedJsonDeserializers
    }
  }

  override def deserializeMessage(data: Array[Byte]): Any = {
    val json = new String(data, Charset.forName("UTF-8")).parseJson
    val jsObject = json.asJson
    val fields = jsObject.fields
    import spray.json.DefaultJsonProtocol._
    val messageType = getField(fields, "messageType").as[String]
    val body = getField(fields, "messageBody").asJson()
    deserialize(messageType, body)
  }

  def orElse(next: JsonMQDeserializer): JsonMQDeserializer =
    new JsonMQDeserializer(jsonDeserializers, Some(next))

  private def getField(fields: Map[String, Json], fieldName: String): Json = {
    try {
      fields(fieldName)
    } catch {
      case e: NoSuchElementException =>
        throw DeserializationException(s"Missing field: $fieldName", e)
    }
  }
}
