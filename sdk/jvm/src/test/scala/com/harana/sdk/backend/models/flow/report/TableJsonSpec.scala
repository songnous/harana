package com.harana.sdk.backend.models.flow.report

import com.harana.sdk.backend.models.designer.flow.report.factory.TableTestFactory
import com.harana.sdk.backend.models.flow.report.factory.TableTestFactory
import com.harana.sdk.shared.models.flow.report.Table
import com.harana.sdk.shared.models.flow.utils.ColumnType
import io.circe.Json
import io.circe.syntax.EncoderOps
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec


class TableJsonSpec extends AnyWordSpec with Matchers with TableTestFactory {

  "Table" should {

    "serialize" when {
      val rowNames = List("rowName1", "rowName2")
      val columnNames = List("A", "B")
      val columnTypes = List(ColumnType.String, ColumnType.Numeric)
      val values = List(List(Some("11"), None), List(None, Some("34")))

      "columnsNames specified" in {
        val json = testTableWithLabels(Some(columnNames), columnTypes, None, values).asJson
        json shouldBe jsonTable(Some(columnNames), columnTypes, None, values)
      }

      "rowsNames specified" in {
        val json = testTableWithLabels(None, columnTypes, Some(rowNames), values).asJson
        json shouldBe jsonTable(None, columnTypes, Some(rowNames), values)
      }

      "rowsNames, columnNames and columTypes specified" in {
        val json = testTableWithLabels(Some(columnNames), columnTypes, Some(rowNames), values).asJson
        json shouldBe jsonTable(Some(columnNames), columnTypes, Some(rowNames), values)
      }

      "is empty" in {
        val json = testEmptyTable.asJson
        json shouldBe jsonTable(None, List(), None, List())
      }
    }
    "deserialize" when {

      "filled table" in {
        val columnNames = Some(List("A", "B"))
        val rowNames = Some(List("1", "2"))
        val columnTypes = List(ColumnType.String, ColumnType.Numeric)
        val values = List(List(Some("a"), Some("1")), List(Some("b"), Some("2")))
        val json = jsonTable(columnNames, columnTypes, rowNames, values)
        json.as[Table] shouldBe testTableWithLabels(columnNames, columnTypes, rowNames, values)
      }

      "empty table" in {
        val json = jsonTable(None, List(), None, List())
        json.as[Table] shouldBe testTableWithLabels(None, List(), None, List())
      }
    }
  }

  private def jsonTable(columnsNames: Option[List[String]], columnTypes: List[ColumnType], rowsNames: Option[List[String]], values: List[List[Option[String]]]) =
    Map(
      "name"        -> Json.fromString(TableTestFactory.tableName),
      "description" -> Json.fromString(TableTestFactory.tableDescription),
      "columnNames" -> toJson(columnsNames),
      "columnTypes" -> toJson(Some(columnTypes.map(_.toString))),
      "rowNames"    -> toJson(rowsNames),
      "values"      -> Seq(values.map(row => Seq(row.map(op => op.map(_)).getOrElse(Json.Null)))).asJson
    ).asJson

  def toJson(values: Option[List[String]]): Json =
    values
      .map(values => Seq(values.map(_)).toVector)
      .getOrElse(Json.Null)

}
