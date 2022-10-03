package com.harana.sdk.shared.models.flow

import com.harana.sdk.shared.models.flow.actionobjects.ActionObjectInfo
import com.harana.sdk.shared.models.flow.report.ReportContent
import com.harana.sdk.shared.models.flow.utils.Id
import io.circe.generic.JsonCodec

@JsonCodec
case class EntitiesMap(entities: Map[Id, EntitiesMap.Entry] = Map()) {
  def subMap(keys: Set[Id]) = EntitiesMap(keys.intersect(entities.keySet).map(key => key -> entities(key)).toMap)
}

object EntitiesMap {

  @JsonCodec
  case class Entry(className: String, report: Option[ReportContent] = None)

  def apply(results: Map[Id, ActionObjectInfo], reports: Map[Id, ReportContent]): EntitiesMap = {
    EntitiesMap(results.map { case (id, entity) =>
      val entry = EntitiesMap.Entry(entity.getClass.getCanonicalName, reports.get(id))
      (id, entry)
    })
  }
}