package com.harana.workflowexecutor.communication.mq.serialization.json

import io.circe.Json

import scala.reflect.ClassTag

class DefaultJsonMessageSerializer[T : JsonWriter : ClassTag](typeName: String)
  extends JsonMessageSerializer {

  val serialize: PartialFunction[Any, Json] = {
    case o if isHandled(o) => handle(o.asInstanceOf[T])
  }

  private def isHandled(obj: Any): Boolean = implicitly[ClassTag[T]].runtimeClass.isInstance(obj)

  private def handle(body: T) = Json(
    "messageType" -> JsString(typeName),
    "messageBody" -> body.asJson
  )
}
