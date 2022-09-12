package com.harana.sdk.backend.models.flow.report

import com.harana.sdk.shared.models.flow.report.Table
import com.harana.sdk.shared.models.flow.utils.ColumnType
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec


class TableSpec extends AnyWordSpec with Matchers {

  "Table" should {
    "throw IllegalArgumentException" when {

      "created with columnNames and columnTypes of different size" in {
        an[IllegalArgumentException] should be thrownBy
          Table("Name", "Description", Some(List("col1", "col2")), List(ColumnType.String, ColumnType.String, ColumnType.String), None, List(List(Some("v1"), None, None)))
      }
      "created one data row of size different than columnTypes size" in {
        an[IllegalArgumentException] should be thrownBy
          Table("Name", "Description", Some(List("col1", "col2", "col3")), List(ColumnType.String, ColumnType.String, ColumnType.String), None, List(List(Some("v1"), None)))
      }
    }
    "get created" when {
      "no column names are passed" in {
        Table("Name", "Description", None, List(ColumnType.String, ColumnType.String, ColumnType.String), None, List(List(Some("v1"), None, None)))
        info("Table created")
      }
      "no data rows are passed" in {
        Table("Name", "Description", None, List(ColumnType.String, ColumnType.String, ColumnType.String), None, List())
        info("Table created")
      }
    }
  }
}
