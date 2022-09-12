package com.harana.sdk.backend.models.flow.report.factory

import com.harana.sdk.shared.models.flow.report.Table
import com.harana.sdk.shared.models.flow.utils.ColumnType

trait TableTestFactory {

  def testTableWithLabels(columnNames: Option[List[String]], columnTypes: List[ColumnType], rowNames: Option[List[String]], values: List[List[Option[String]]]) =
    Table(TableTestFactory.tableName, TableTestFactory.tableDescription, columnNames, columnTypes, rowNames, values)

  def testEmptyTable =
    Table(TableTestFactory.tableName, TableTestFactory.tableDescription, None, List(), None, List())

}

object TableTestFactory extends TableTestFactory {
  val tableName = "table name"
  val tableDescription = "table description"
}
