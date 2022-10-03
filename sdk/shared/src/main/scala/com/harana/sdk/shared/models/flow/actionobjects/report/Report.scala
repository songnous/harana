package com.harana.sdk.shared.models.flow.actionobjects.report

import com.harana.sdk.shared.models.flow.actionobjects.ActionObjectInfo
import com.harana.sdk.shared.models.flow.report.ReportType.Empty
import com.harana.sdk.shared.models.flow.report.{ReportContent, ReportType, Table}
import com.harana.sdk.shared.models.flow.report
import io.circe.generic.JsonCodec

@JsonCodec
case class Report(content: ReportContent = report.ReportContent("Empty Report", Empty)) extends ActionObjectInfo {

  val id = "6FC7E2D0-BE11-4DFE-833D-A055FB769D3E"

  def this() = this(ReportContent("Empty Report", ReportType.Empty))

  def withReportName(newName: String) = copy(content.copy(name = newName))
  def withReportType(newReportType: ReportType) = copy(content.copy(reportType = newReportType))

  def withAdditionalTable(table: Table, at: Int = 0) = {
    require(at <= content.tables.length && at >= 0, s"Table can be placed in possible position: [0; ${content.tables.length}]")
    val (left, right) = content.tables.splitAt(at)
    val newTables = left ++ Seq(table) ++ right
    copy(content.copy(tables = newTables))
  }

  override def report(extended: Boolean = true) =
    this
}
