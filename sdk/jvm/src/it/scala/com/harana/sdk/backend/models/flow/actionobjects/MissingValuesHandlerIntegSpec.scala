package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.shared.models.designer.flow.actionobjects.MissingValuesHandler.{EmptyColumnsStrategy, MissingValueIndicatorChoice}
import com.harana.sdk.backend.models.flow.IntegratedTestSupport
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers.TransformerSerialization
import com.harana.sdk.backend.models.flow.actions.exceptions.{MultipleTypesReplacementError, ValueConversionError}
import com.harana.sdk.shared.models.designer.flow.actionobjects
import com.harana.sdk.shared.models.flow.parameters.exceptions.EmptyColumnPrefixNameError$
import com.harana.sdk.shared.models.flow.parameters.selections.{IndexColumnSelection, IndexRangeColumnSelection, MultipleColumnSelection, TypeColumnSelection}
import com.harana.sdk.shared.models.flow.utils.ColumnType
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._
import org.joda.time.DateTime
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import java.sql.Timestamp

class MissingValuesHandlerIntegSpec
    extends IntegratedTestSupport
    with ScalaCheckDrivenPropertyChecks
    with Matchers
    with TransformerSerialization {

  import IntegratedTestSupport._
  import TransformerSerialization._

  trait TestData {

    val schema = StructType(
      List(
        StructField("value-1", DoubleType, nullable = true),
        StructField("value2", StringType, nullable = true),
        StructField("value3", StringType, nullable = true)
      )
    )

    val uut = new MissingValuesHandler

    val allColumnsInSchemaSelection = MultipleColumnSelection(
      Vector(IndexRangeColumnSelection(Some(0), Some(schema.fields.length - 1)))
    )

  }

  "MissingValuesHandler" should {
    "remove rows with null, NaN or undefined values while using REMOVE_ROW strategy" in {
      val values = Seq(
        Row(1.0, null, "undefined"),
        Row(2.0, null, "some string 2"),
        Row(Double.NaN, 3.0, "some string 3"),
        Row(4.0, 4.0, "some string 4"),
        Row(5.0, 5.0, "NaN"),
        Row(null, null, "some string 6")
      )

      val df = createDataFrame(
        values,
        StructType(
          List(
            StructField("value-1", DoubleType, nullable = true),
            StructField("value2", DoubleType, nullable = true),
            StructField("value3", StringType, nullable = true)
          )
        )
      )

      val columnSelection = MultipleColumnSelection(Vector(IndexColumnSelection(Set(0, 2))))

      val handler =
        new MissingValuesHandler()
          .setUserDefinedMissingValues(Seq("NaN", "undefined"))
          .setSelectedColumns(columnSelection)
          .setStrategy(actionobjects.MissingValuesHandler.Strategy.RemoveRow())
          .setMissingValueIndicator(
            actionobjects.MissingValuesHandler.MissingValueIndicatorChoice
              .Yes()
              .setIndicatorPrefix("prefix_")
          )

      val resultDf = executeTransformer(handler, df)

      val expectedDf = createDataFrame(
        Seq(Row(2.0, null, "some string 2", false, false), Row(4.0, 4.0, "some string 4", false, false)),
        StructType(
          List(
            StructField("value-1", DoubleType, nullable = true),
            StructField("value2", DoubleType, nullable = true),
            StructField("value3", StringType, nullable = true),
            StructField("prefix_value-1", BooleanType, nullable = true),
            StructField("prefix_value3", BooleanType, nullable = true)
          )
        )
      )

      assertDataFramesEqual(resultDf, expectedDf)
    }

    "remove columns with null, NaN (numeric) or -1.0 values while using REMOVE_COLUMN strategy" in {
      val values = Seq(
        Row(1.0, Double.NaN, "ddd", null),
        Row(-1.0, 2.0, "eee", null),
        Row(3.0, 3.0, "fff", null),
        Row(4.0, 4.0, "NaN", null),
        Row(5.0, 5.0, "ggg", null)
      )

      val df = createDataFrame(
        values,
        StructType(
          List(
            StructField("value-1", DoubleType, nullable = true),
            StructField("value2", DoubleType, nullable = true),
            StructField("value3", StringType, nullable = true),
            StructField("value4", StringType, nullable = true)
          )
        )
      )

      val columnSelection = MultipleColumnSelection(Vector(IndexRangeColumnSelection(Some(0), Some(2))))
      val resultDf        = executeTransformer(
        new MissingValuesHandler()
          .setUserDefinedMissingValues(Seq("-1.0"))
          .setSelectedColumns(columnSelection)
          .setStrategy(actionobjects.MissingValuesHandler.Strategy.RemoveColumn())
          .setMissingValueIndicator(
            actionobjects.MissingValuesHandler.MissingValueIndicatorChoice
              .Yes()
              .setIndicatorPrefix("prefix_")
          ),
        df
      )

      val expectedDf = createDataFrame(
        Seq(
          Row("ddd", null, false, true, false),
          Row("eee", null, true, false, false),
          Row("fff", null, false, false, false),
          Row("NaN", null, false, false, false),
          Row("ggg", null, false, false, false)
        ),
        StructType(
          List(
            StructField("value3", StringType, nullable = true),
            StructField("value4", StringType, nullable = true),
            StructField("prefix_value-1", BooleanType, nullable = true),
            StructField("prefix_value2", BooleanType, nullable = true),
            StructField("prefix_value3", BooleanType, nullable = true)
          )
        )
      )

      assertDataFramesEqual(resultDf, expectedDf)
    }

    "replace numerics while using REPLACE_WITH_CUSTOM_VALUE strategy" in {
      val values = Seq(
        Row(1.toByte, BigDecimal("1.0"), 1.0, 1.0f, 1, 1L, 1.toShort, null),
        Row(2.toByte, BigDecimal("2.0"), 2.0, 2.0f, 2, 2L, 2.toShort, null),
        Row(null, null, Double.NaN, null, null, null, null, null),
        Row(4.toByte, BigDecimal("4.0"), 4.0, Float.NaN, 4, 4L, 4.toShort, null)
      )

      val df = createDataFrame(
        values,
        StructType(
          List(
            StructField("value-1", ByteType, nullable = true),
            StructField("value2", DecimalType(5, 3), nullable = true),
            StructField("value3", DoubleType, nullable = true),
            StructField("value4", FloatType, nullable = true),
            StructField("value5", IntegerType, nullable = true),
            StructField("value6", LongType, nullable = true),
            StructField("value7", ShortType, nullable = true),
            StructField("value8", StringType, nullable = true)
          )
        )
      )

      val columnSelection = MultipleColumnSelection(Vector(TypeColumnSelection(Set(ColumnType.Numeric))))

      val handler = new MissingValuesHandler()
        .setUserDefinedMissingValues(Seq())
        .setSelectedColumns(columnSelection)
        .setStrategy(actionobjects.MissingValuesHandler.Strategy.ReplaceWithCustomValue().setCustomValue("3"))
        .setMissingValueIndicator(actionobjects.MissingValuesHandler.MissingValueIndicatorChoice.Yes().setIndicatorPrefix("prefix_"))

      val resultDf = executeTransformer(handler, df)

      val expectedDf = createDataFrame(
        Seq(
          Row(
            1.toByte,
            BigDecimal("1.0"),
            1.0,
            1.0f,
            1,
            1L,
            1.toShort,
            null,
            false,
            false,
            false,
            false,
            false,
            false,
            false
          ),
          Row(
            2.toByte,
            BigDecimal("2.0"),
            2.0,
            2.0f,
            2,
            2L,
            2.toShort,
            null,
            false,
            false,
            false,
            false,
            false,
            false,
            false
          ),
          Row(3.toByte, BigDecimal("3"), 3.0, 3.0f, 3, 3L, 3.toShort, null, true, true, true, true, true, true, true),
          Row(
            4.toByte,
            BigDecimal("4.0"),
            4.0,
            3.0f,
            4,
            4L,
            4.toShort,
            null,
            false,
            false,
            false,
            true,
            false,
            false,
            false
          )
        ),
        StructType(
          List(
            StructField("value-1", ByteType, nullable = true),
            StructField("value2", DecimalType(5, 3), nullable = true),
            StructField("value3", DoubleType, nullable = true),
            StructField("value4", FloatType, nullable = true),
            StructField("value5", IntegerType, nullable = true),
            StructField("value6", LongType, nullable = true),
            StructField("value7", ShortType, nullable = true),
            StructField("value8", StringType, nullable = true),
            StructField("prefix_value-1", BooleanType, nullable = true),
            StructField("prefix_value2", BooleanType, nullable = true),
            StructField("prefix_value3", BooleanType, nullable = true),
            StructField("prefix_value4", BooleanType, nullable = true),
            StructField("prefix_value5", BooleanType, nullable = true),
            StructField("prefix_value6", BooleanType, nullable = true),
            StructField("prefix_value7", BooleanType, nullable = true)
          )
        )
      )

      assertDataFramesEqual(resultDf, expectedDf)
    }

    "replace strings while using REPLACE_WITH_CUSTOM_VALUE strategy" in {
      val values = Seq(
        Row("aaa", null),
        Row("missing", null),
        Row(null, null),
        Row("ddd", null)
      )

      val df = createDataFrame(
        values,
        StructType(
          List(
            StructField("value-1", StringType, nullable = true),
            StructField("value2", StringType, nullable = true)
          )
        )
      )

      val columnSelection = MultipleColumnSelection(Vector(IndexRangeColumnSelection(Some(0), Some(0))))
      val resultDf        = executeTransformer(
        new MissingValuesHandler()
          .setUserDefinedMissingValues(Seq("missing"))
          .setSelectedColumns(columnSelection)
          .setStrategy(
            actionobjects.MissingValuesHandler.Strategy
              .ReplaceWithCustomValue()
              .setCustomValue("replaced missing")
          ),
        df
      )

      val expectedDf = createDataFrame(
        Seq(
          Row("aaa", null),
          Row("replaced missing", null),
          Row("replaced missing", null),
          Row("ddd", null)
        ),
        StructType(
          List(
            StructField("value-1", StringType, nullable = true),
            StructField("value2", StringType, nullable = true)
          )
        )
      )

      assertDataFramesEqual(resultDf, expectedDf)
    }

    "replace null and false booleans while using REPLACE_WITH_CUSTOM_VALUE strategy" in {
      val values = Seq(
        Row(true, null),
        Row(false, null),
        Row(null, null),
        Row(false, null)
      )

      val df = createDataFrame(
        values,
        StructType(
          List(
            StructField("value-1", BooleanType, nullable = true),
            StructField("value2", StringType, nullable = true)
          )
        )
      )

      val columnSelection = MultipleColumnSelection(Vector(IndexRangeColumnSelection(Some(0), Some(0))))
      val resultDf        = executeTransformer(
        new MissingValuesHandler()
          .setUserDefinedMissingValues(Seq("false"))
          .setSelectedColumns(columnSelection)
          .setStrategy(
            actionobjects.MissingValuesHandler.Strategy
              .ReplaceWithCustomValue()
              .setCustomValue("true")
          ),
        df
      )

      val expectedDf = createDataFrame(
        Seq(
          Row(true, null),
          Row(true, null),
          Row(true, null),
          Row(true, null)
        ),
        StructType(
          List(StructField("value-1", BooleanType, nullable = true), StructField("value2", StringType, nullable = true))
        )
      )

      assertDataFramesEqual(resultDf, expectedDf)
    }

    "replace timestamps while using REPLACE_WITH_CUSTOM_VALUE strategy" in {

      val base = new DateTime(2015, 3, 30, 15, 25)

      val missing   = new Timestamp(base.getMillis + 1e6.toLong)
      val t2        = new Timestamp(base.getMillis + 2e6.toLong)
      val t3        = new Timestamp(base.getMillis + 3e6.toLong)
      val toReplace = new Timestamp(base.getMillis)

      val values = Seq(
        Row(missing, null),
        Row(t2, null),
        Row(t3, null),
        Row(null, null)
      )

      val df = createDataFrame(
        values,
        StructType(
          List(
            StructField("value-1", TimestampType, nullable = true),
            StructField("value2", StringType, nullable = true)
          )
        )
      )

      val columnSelection = MultipleColumnSelection(Vector(IndexRangeColumnSelection(Some(0), Some(0))))
      val resultDf        = executeTransformer(
        new MissingValuesHandler()
          .setUserDefinedMissingValues(Seq(missing.toString))
          .setSelectedColumns(columnSelection)
          .setStrategy(
            actionobjects.MissingValuesHandler.Strategy
              .ReplaceWithCustomValue()
              .setCustomValue(toReplace.toString)
          ),
        df
      )

      val expectedDf = createDataFrame(
        Seq(
          Row(toReplace, null),
          Row(t2, null),
          Row(t3, null),
          Row(toReplace, null)
        ),
        StructType(
          List(
            StructField("value-1", TimestampType, nullable = true),
            StructField("value2", StringType, nullable = true)
          )
        )
      )

      assertDataFramesEqual(resultDf, expectedDf)
    }

    "throw an exception with different types using REPLACE_WITH_CUSTOM_VALUE strategy" in {
      val values = Seq(
        Row(1.0, "aaa", null),
        Row(2.0, "bbb", null),
        Row(null, "ccc", null),
        Row(4.0, null, null)
      )

      val df = createDataFrame(
        values,
        StructType(
          List(
            StructField("value-1", DoubleType, nullable = true),
            StructField("value2", StringType, nullable = true),
            StructField("value3", StringType, nullable = true)
          )
        )
      )

      val columnSelection = MultipleColumnSelection(Vector(IndexRangeColumnSelection(Some(0), Some(1))))

      an[MultipleTypesReplacementError] should be thrownBy executeTransformer(
        new MissingValuesHandler()
          .setUserDefinedMissingValues(Seq())
          .setSelectedColumns(columnSelection)
          .setStrategy(
            actionobjects.MissingValuesHandler.Strategy
              .ReplaceWithCustomValue()
              .setCustomValue("3")
          ),
        df
      )
    }

    "throw an exception with invalid value using REPLACE_WITH_CUSTOM_VALUE strategy" in {
      val values = Seq(
        Row(1.0, null),
        Row(2.0, null),
        Row(null, null),
        Row(4.0, null)
      )

      val df = createDataFrame(
        values,
        StructType(
          List(
            StructField("value-1", DoubleType, nullable = true),
            StructField("value2", StringType, nullable = true)
          )
        )
      )

      val columnSelection = MultipleColumnSelection(Vector(IndexRangeColumnSelection(Some(0), Some(0))))

      an[ValueConversionError] should be thrownBy executeTransformer(
        new MissingValuesHandler()
          .setUserDefinedMissingValues(Seq())
          .setSelectedColumns(columnSelection)
          .setStrategy(
            actionobjects.MissingValuesHandler.Strategy
              .ReplaceWithCustomValue()
              .setCustomValue("aaaa")
          ),
        df
      )
    }

    "replace null with mode using REPLACE_WITH_MODE strategy in RETAIN mode" in {
      val values = Seq(
        Row(1.0, null, null),
        Row(null, "aaa", null),
        Row(Double.NaN, "aaa", null),
        Row(1.0, "undefined", null),
        Row(100.0, "bbb", null)
      )

      val df = createDataFrame(
        values,
        StructType(
          List(
            StructField("value-1", DoubleType, nullable = true),
            StructField("value2", StringType, nullable = true),
            StructField("value3", StringType, nullable = true)
          )
        )
      )

      val columnSelection = MultipleColumnSelection(Vector(IndexRangeColumnSelection(Some(0), Some(2))))

      val handler =
        new MissingValuesHandler()
          .setUserDefinedMissingValues(Seq("undefined"))
          .setSelectedColumns(columnSelection)
          .setStrategy(
            actionobjects.MissingValuesHandler.Strategy
              .ReplaceWithMode()
              .setEmptyColumnStrategy(actionobjects.MissingValuesHandler.EmptyColumnsStrategy.RetainEmptyColumns())
          )
          .setMissingValueIndicator(
            actionobjects.MissingValuesHandler.MissingValueIndicatorChoice
              .Yes()
              .setIndicatorPrefix("prefix_")
          )

      val resultDf = executeTransformer(handler, df)

      val expectedDf = createDataFrame(
        Seq(
          Row(1.0, "aaa", null, false, true, true),
          Row(1.0, "aaa", null, true, false, true),
          Row(1.0, "aaa", null, true, false, true),
          Row(1.0, "aaa", null, false, true, true),
          Row(100.0, "bbb", null, false, false, true)
        ),
        StructType(
          List(
            StructField("value-1", DoubleType, nullable = true),
            StructField("value2", StringType, nullable = true),
            StructField("value3", StringType, nullable = true),
            StructField("prefix_value-1", BooleanType, nullable = true),
            StructField("prefix_value2", BooleanType, nullable = true),
            StructField("prefix_value3", BooleanType, nullable = true)
          )
        )
      )

      assertDataFramesEqual(resultDf, expectedDf)
    }

    "replace with mode using REPLACE_WITH_MODE strategy in REMOVE mode" in {
      val values = Seq(
        Row(1.0, Double.NaN, "red", "undefined"),
        Row(null, 2.0, "blue", "NA"),
        Row(1.0, 2.0, "blue", null),
        Row(1.0, 2.0, "blue", "missing"),
        Row(100.0, 100.0, null, null)
      )

      val rawDf = createDataFrame(
        values,
        StructType(
          List(
            StructField("value-1", DoubleType, nullable = true),
            StructField("value2", DoubleType, nullable = true),
            StructField("value3", StringType, nullable = true),
            StructField("value4", StringType, nullable = true)
          )
        )
      )

      val df = rawDf

      val columnSelection = MultipleColumnSelection(Vector(IndexRangeColumnSelection(Some(0), Some(3))))
      val resultDf        = executeTransformer(
        new MissingValuesHandler()
          .setUserDefinedMissingValues(Seq("missing", "NA", "undefined"))
          .setSelectedColumns(columnSelection)
          .setStrategy(
            actionobjects.MissingValuesHandler.Strategy
              .ReplaceWithMode()
              .setEmptyColumnStrategy(actionobjects.MissingValuesHandler.EmptyColumnsStrategy.RemoveEmptyColumns())
          ),
        df
      )

      val expectedDf = createDataFrame(
        Seq(
          Row(1.0, 2.0, "red"),
          Row(1.0, 2.0, "blue"),
          Row(1.0, 2.0, "blue"),
          Row(1.0, 2.0, "blue"),
          Row(100.0, 100.0, "blue")
        ),
        StructType(
          List(
            StructField("value-1", DoubleType, nullable = true),
            StructField("value2", DoubleType, nullable = true),
            StructField("value3", StringType, nullable = true)
          )
        )
      )

      assertDataFramesEqual(resultDf, expectedDf)
    }

  }

  "with REPLACE_WITH_CUSTOM_VALUE strategy and no MissingValuesIndicator, " +
    "transformSchema should return unmodified schema" in {
      val schema = StructType(
        List(
          StructField("value-1", DoubleType, nullable = true),
          StructField("value2", StringType, nullable = true),
          StructField("value3", StringType, nullable = true)
        )
      )

      val columnSelection = MultipleColumnSelection(Vector(IndexRangeColumnSelection(Some(0), Some(1))))

      val transformation = new MissingValuesHandler()
        .setUserDefinedMissingValues(Seq())
        .setSelectedColumns(columnSelection)
        .setStrategy(
          actionobjects.MissingValuesHandler.Strategy
            .ReplaceWithCustomValue()
            .setCustomValue("aaaa")
        )

      transformation._transformSchema(schema) shouldBe Some(schema)
    }

  "with REMOVE_ROWS strategy and MissingValuesIndicator set, " +
    "transformSchema should return modified schema" in {
      val schema = StructType(
        List(
          StructField("value-1", DoubleType, nullable = true),
          StructField("value2", DoubleType, nullable = true)
        )
      )

      val columnSelection = MultipleColumnSelection(Vector(IndexRangeColumnSelection(Some(0), Some(0))))

      val transformation = new MissingValuesHandler()
        .setUserDefinedMissingValues(Seq())
        .setSelectedColumns(columnSelection)
        .setStrategy(actionobjects.MissingValuesHandler.Strategy.RemoveRow())
        .setMissingValueIndicator(
          actionobjects.MissingValuesHandler.MissingValueIndicatorChoice
            .Yes()
            .setIndicatorPrefix("prefix_")
        )

      val expectedSchema = StructType(
        List(
          StructField("value-1", DoubleType, nullable = true),
          StructField("value2", DoubleType, nullable = true),
          StructField("prefix_value-1", BooleanType, nullable = false)
        )
      )

      transformation._transformSchema(schema) shouldBe Some(expectedSchema)
    }

  "with REMOVE_COLUMN strategy transformSchema should return None" in {
    new TestData {
      val columnSelection = MultipleColumnSelection(Vector(IndexRangeColumnSelection(Some(0), Some(1))))

      val transformation = new MissingValuesHandler()
        .setUserDefinedMissingValues(Seq())
        .setSelectedColumns(columnSelection)
        .setStrategy(actionobjects.MissingValuesHandler.Strategy.RemoveColumn())
        .setMissingValueIndicator(
          actionobjects.MissingValuesHandler.MissingValueIndicatorChoice
            .Yes()
            .setIndicatorPrefix("prefix_")
        )

      transformation._transformSchema(schema) shouldBe None
    }
  }

  "not throw an error" when {
    "all DataFrame values are missing for RemoveEmptyColumns strategy" in {
      new TestData {
        val strategy = actionobjects.MissingValuesHandler.Strategy.ReplaceWithMode()

        strategy.setEmptyColumnStrategy(EmptyColumnsStrategy.RemoveEmptyColumns())
        uut
          .setUserDefinedMissingValues(Seq())
          .setSelectedColumns(allColumnsInSchemaSelection)
          .setStrategy(strategy)

        val df = createDataFrame(
          Seq(
            Row(null, null, null),
            Row(null, null, null)
          ),
          schema
        )

        val resultDf   = executeTransformer(uut, df)
        val expectedDf = createDataFrame(Seq(Row(), Row()), StructType(Seq()))

        assertDataFramesEqual(resultDf, expectedDf)
      }
    }
  }

  "should fail param validation" when {
    "indicator column prefix not set" in {
      new TestData {
        uut
          .setSelectedColumns(allColumnsInSchemaSelection)
          .setMissingValueIndicator(MissingValueIndicatorChoice.Yes())

        uut.validateParameters should contain(EmptyColumnPrefixNameError$)

      }
    }
  }

  def executeTransformer(op: MissingValuesHandler, df: DataFrame) =
    op.applyTransformationAndSerialization(tempDir, df)

}
