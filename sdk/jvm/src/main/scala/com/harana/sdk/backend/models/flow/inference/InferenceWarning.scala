package com.harana.sdk.backend.models.flow.inference

import io.circe.{Decoder, Encoder, HCursor, Json}

trait InferenceWarning {
  val message: String
}

object InferenceWarning {
  implicit val encoder: Encoder[InferenceWarning] = (obj: InferenceWarning) => Json.obj(
    ("message", Json.fromString(obj.message))
  )

  implicit val decoder: Decoder[InferenceWarning] = (c: HCursor) => for {
    messageStr <- c.downField("message").as[String]
  } yield
    new InferenceWarning { val message = messageStr }

}