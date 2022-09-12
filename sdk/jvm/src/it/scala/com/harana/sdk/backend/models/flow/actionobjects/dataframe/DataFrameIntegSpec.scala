package com.harana.sdk.backend.models.flow.actionobjects.dataframe

import com.harana.sdk.backend.models.flow.IntegratedTestSupport
import org.apache.spark.sql.types._
import com.harana.sdk.backend.models.flow.actions.exceptions.ColumnsDoNotExistError
import com.harana.sdk.shared.models.flow.parameters.selections.{IndexColumnSelection, MultipleColumnSelection, NameColumnSelection, TypeColumnSelection}
import com.harana.sdk.shared.models.flow.utils.ColumnType

class DataFrameIntegSpec extends IntegratedTestSupport {

  "DataFrame" should {
    def schema: StructType = StructType(
      List(
        StructField("a", ArrayType(BooleanType)),
        StructField("b", BinaryType),
        StructField("c", BooleanType),
        StructField("d", ByteType),
        StructField("e", DateType),
        StructField("f", DecimalType(5, 5)),
        StructField("g", DoubleType),
        StructField("h", FloatType),
        StructField("i", IntegerType),
        StructField("j", LongType),
        StructField("k", MapType(StringType, StringType)),
        StructField("l", NullType),
        StructField("m", ShortType),
        StructField("n", StringType),
        StructField("o", StructType(Seq(StructField("n", StringType)))),
        StructField("p", TimestampType)
      )
    )

    def dataFrame = createDataFrame(Seq.empty, schema)

    "return correct sequence of columns' names based on column selection" when {

      "many selectors are used" in {
        val selection = MultipleColumnSelection(
          Vector(
            NameColumnSelection(Set("a")),
            IndexColumnSelection(Set(1, 3)),
            TypeColumnSelection(Set(ColumnType.String, ColumnType.timestamp))
          ),
          false
        )
        dataFrame.getColumnNames(selection) shouldBe Seq("a", "b", "d", "n", "p")
      }

      "columns are selected in different order" in {
        val selection = MultipleColumnSelection(
          Vector(
            NameColumnSelection(Set("c")),
            NameColumnSelection(Set("a")),
            NameColumnSelection(Set("b"))
          ),
          false
        )
        dataFrame.getColumnNames(selection) shouldBe Seq("a", "b", "c")
      }

      def selectSingleType(columnType: ColumnType): Seq[String] = {
        val selection = MultipleColumnSelection(Vector(TypeColumnSelection(Set(columnType))), false)
        dataFrame.getColumnNames(selection)
      }

      "boolean type is selected" in {
        selectSingleType(ColumnType.Boolean) shouldBe Seq("c")
      }

      "string type is selected" in {
        selectSingleType(ColumnType.String) shouldBe Seq("n")
      }

      "numeric type is selected" in {
        selectSingleType(ColumnType.Numeric) shouldBe Seq("d", "f", "g", "h", "i", "j", "m")
      }

      "timestamp type is selected" in {
        selectSingleType(ColumnType.timestamp) shouldBe Seq("p")
      }

      "excluding selector is used" in {
        val selection = MultipleColumnSelection(
          Vector(NameColumnSelection(Set("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m"))),
          true
        )
        dataFrame.getColumnNames(selection) shouldBe Seq("n", "o", "p")
      }
    }

    "throw an exception" when {
      "non-existing column name was selected" in {
        intercept[ColumnsDoNotExistError] {
          val selection = MultipleColumnSelection(Vector(NameColumnSelection(Set("no such column"))), false)
          dataFrame.getColumnNames(selection)
        }
        ()
      }
      "index out of bounds was selected" in {
        intercept[ColumnsDoNotExistError] {
          val selection = MultipleColumnSelection(Vector(IndexColumnSelection(Set(20))), false)
          dataFrame.getColumnNames(selection)
        }
        ()
      }
    }
  }
}
