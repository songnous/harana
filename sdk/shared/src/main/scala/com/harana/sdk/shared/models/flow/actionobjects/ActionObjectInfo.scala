package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.actionobjects.descriptions.HasInferenceResult
import com.harana.sdk.shared.models.flow.actionobjects.report.Report
import com.harana.sdk.shared.models.flow.catalog.Catalog
import com.harana.sdk.shared.models.flow.utils
import io.circe.{Decoder, Encoder, HCursor, Json}

trait ActionObjectInfo extends HasInferenceResult {
  val id: utils.Id
  def report(extended: Boolean = true) = Report()
}

object ActionObjectInfo {

  implicit val encoder: Encoder[ActionObjectInfo] = (a: ActionObjectInfo) =>
    Json.fromString(a.id.toString)

  implicit val decoder: Decoder[ActionObjectInfo] = (c: HCursor) => for {
    id <- c.as[String]
  } yield {
    Catalog.objectsByIdMap(id)()
  }

}