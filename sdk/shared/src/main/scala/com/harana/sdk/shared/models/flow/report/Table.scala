package com.harana.sdk.shared.models.flow.report

import com.harana.sdk.shared.models.flow.utils.ColumnType
import io.circe.generic.JsonCodec

@JsonCodec
case class Table(
    name: String,
    description: String,
    columnNames: Option[List[String]],
    columnTypes: List[ColumnType],
    rowNames: Option[List[String]],
    values: List[List[Option[String]]]
) {

  require(
    columnNames match {
      case Some(columnNamesList) => columnNamesList.size == columnTypes.size
      case _                     => true
    },
    "columnNames and columnTypes should have the same size"
  )
  require(
    !values.exists(_.length != columnTypes.length),
    "at least one data row has size different than columnTypes size"
  )

}
