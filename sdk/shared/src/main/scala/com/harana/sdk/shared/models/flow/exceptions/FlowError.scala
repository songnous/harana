package com.harana.sdk.shared.models.flow.exceptions

import io.circe.syntax._
import io.circe.{Decoder, Encoder, HCursor, Json}

trait FlowError extends HaranaError {

  val code = FailureCode.NodeFailure
  val title = "Harana Exception"

}

object FlowError {
  implicit val encoder: Encoder[FlowError] = (a: FlowError) => Json.obj(
    ("message", Json.fromString(a.message)),
    ("details", a.title.asJson),
    ("id", Json.fromString(a.id.toString))
  )

  implicit val decoder: Decoder[FlowError] = (c: HCursor) => for {
    _message <- c.downField("message").as[String]
    _details <- c.downField("details").as[Map[String, String]]
    _id <- c.downField("id").as[String]
  } yield {
    new FlowError {
      val message = _message
      override val details = _details
      override val id = _id
    }
  }
}