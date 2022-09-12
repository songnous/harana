package com.harana.sdk.shared.models.flow.report

import io.circe.generic.JsonCodec

@JsonCodec
case class ReportContent(
    name: String,
    reportType: ReportType,
    tables: Seq[Table] = Seq.empty,
    distributions: Map[String, Distribution] = Map()) {

  def tableByName(name: String): Option[Table] = tables.find(_.name == name)

}