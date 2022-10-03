package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.report.{ReportType, Table}
import com.harana.sdk.shared.models.flow.utils.{ColumnType, DoubleUtils}
import io.circe.generic.JsonCodec

@JsonCodec
case class MetricValue(name: String, value: Double = Double.NaN) extends ActionObjectInfo {

  val id = "BDBCB3DD-5C73-4FE3-A8F6-136C1521D42F"

  def this() = this(null, Double.NaN)

  override def report(extended: Boolean = true) =
    super
      .report(extended)
      .withReportName("Report for Metric Value")
      .withReportType(ReportType.MetricValue)
      .withAdditionalTable(
        Table(
          name = "Metric Value",
          description = "",
          columnNames = Some(List(name)),
          columnTypes = List(ColumnType.String),
          rowNames = None,
          values = List(List(Some(DoubleUtils.double2String(value))))
        )
      )
}

object MetricValue {
  def forInference(name: String) = MetricValue(name, Double.NaN)
}