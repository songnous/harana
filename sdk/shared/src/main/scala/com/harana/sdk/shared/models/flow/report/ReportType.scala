package com.harana.sdk.shared.models.flow.report

import enumeratum._

sealed trait ReportType extends EnumEntry

case object ReportType extends Enum[ReportType] with CirceEnum[ReportType] {
  case object Empty extends ReportType
  case object Estimator extends ReportType
  case object Evaluator extends ReportType
  case object DataFrameFull extends ReportType
  case object DataFrameSimplified extends ReportType
  case object GridSearch extends ReportType
  case object Model extends ReportType
  case object MetricValue extends ReportType
  val values = findValues
}
