package com.harana.sdk.backend.models.flow.actionobjects.report

import com.harana.sdk.backend.models.flow.utils.SparkTypeConverter

object ReportUtils {
  val StringPreviewMaxLength = 300

  def shortenLongStrings(value: String, maxLength: Int = StringPreviewMaxLength) =
    if (value.length < maxLength) value else value.take(maxLength) + "..."

  def shortenLongTableValues(vals: List[List[Option[String]]], maxLength: Int = StringPreviewMaxLength) =
    vals.map(_.map {
      case None         => None
      case Some(strVal) => Some(ReportUtils.shortenLongStrings(strVal, maxLength))
    })

  def formatCell(cell: Any) = SparkTypeConverter.sparkAnyToString(cell)

  def formatValues(values: List[List[Option[Any]]]) = values.map(_.map(_.map(formatCell)))
}
