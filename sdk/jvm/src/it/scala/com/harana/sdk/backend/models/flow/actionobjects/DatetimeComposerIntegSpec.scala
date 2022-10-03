package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.IntegratedTestSupport

import java.sql.Timestamp
import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema
import org.apache.spark.sql.types._
import org.joda.time.DateTime
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers.TransformerSerialization
import com.harana.sdk.backend.models.flow.actiontypes.exceptions.{ColumnDoesNotExistError, WrongColumnTypeError}
import com.harana.sdk.shared.models.flow.actionobjects.DatetimeComposerInfo.TimestampPartColumnChoice.{Hour, Year}
import com.harana.sdk.shared.models.flow.actionobjects.DatetimeComposerInfo.orderedTimestampParts
import com.harana.sdk.shared.models.flow.parameters.selections.{IndexSingleColumnSelection, NameSingleColumnSelection}

class DatetimeComposerIntegSpec extends IntegratedTestSupport with TransformerSerialization {

  private val t1 = new DateTime(2015, 3, 30, 15, 25, 40)

  import DatetimeComposer.TimestampPartColumnChoice._
  import DatetimeComposer.orderedTimestampParts
  import IntegratedTestSupport._
  import TransformerSerialization._

  "DatetimeComposer" should {
    "compose timestamp from timestamp part columns" in {
      val outputName = "timestamp"
      val baseSchema = createSchema
      val expectedSchema = resultSchema(baseSchema, outputName)
      val t2 = t1.plusDays(1)
      val dataFrame = createDataFrame(
        Seq(createUncomposedTimestampRow(baseSchema, t1), createUncomposedTimestampRow(baseSchema, t2)),
        baseSchema
      )
      val expectedDataFrame = createDataFrame(
        Seq(createComposedTimestampRow(expectedSchema, t1), createComposedTimestampRow(expectedSchema, t2)),
        expectedSchema
      )
      shouldComposeTimestamp(dataFrame, expectedDataFrame, outputName)
    }

    "compose timestamp values to the same zone" in {
      val outputName = "timestamp"
      val dataFrame = createDataFrame(Seq(Row(15.0)), StructType(List(StructField(Hour().name, DoubleType))))
      val transformedDataFrame  = composeHour(dataFrame, outputName)
      val List(hour: String, timestamp: String) = transformedDataFrame.report().content.tables.head.values.head.map(_.get)
      timestamp.substring(11, 13) shouldBe hour
    }
  }

  it should {
    "transform schema" in {
      val outputName = "timestamp"
      val schema = createSchema
      val action = actionWithParametersSet(outputName)
      val transformedSchema = action._transformSchema(schema)
      val expectedSchema = resultSchema(schema, outputName)
      expectedSchema shouldBe transformedSchema.get
    }
  }

  it should {
    "throw an exception" when {

      "column selected by name does not exist" in {
        a[ColumnDoesNotExistError] should be thrownBy {
          val action = new DatetimeComposer()
            .setTimestampColumns(Set(Year().setTimestampColumn(NameSingleColumnSelection("wrong_name"))))
            .setOutputColumn("timestamp")
          val dataFrame = createDataFrame(Seq.empty, StructType(List(StructField("id", DoubleType))))
          action._transform(executionContext, dataFrame)
        }
      }

      "column selected by index does not exist" in {
        a[ColumnDoesNotExistError] should be thrownBy {
          val action = new DatetimeComposer()
            .setTimestampColumns(Set(Year().setTimestampColumn(IndexSingleColumnSelection(1))))
            .setOutputColumn("timestamp")
          val dataFrame = createDataFrame(Seq.empty, StructType(List(StructField("id", DoubleType))))
          action._transform(executionContext, dataFrame)
        }
      }
    }
  }

  it should {
    "throw an exception in transform schema" when {

      "column selected by name does not exist" in {
        a[ColumnDoesNotExistError] should be thrownBy {
          val action = new DatetimeComposer()
            .setTimestampColumns(Set(Year().setTimestampColumn(NameSingleColumnSelection("wrong_name"))))
            .setOutputColumn("timestamp")
          val schema = StructType(List(StructField("id", DoubleType)))
          action._transformSchema(schema)
        }
      }

      "column selected by index does not exist" in {
        a[ColumnDoesNotExistError] should be thrownBy {
          val action = new DatetimeComposer()
            .setTimestampColumns(Set(Year().setTimestampColumn(IndexSingleColumnSelection(1))))
            .setOutputColumn("timestamp")
          val schema = StructType(List(StructField("id", DoubleType)))
          action._transformSchema(schema)
        }
      }

      "selected column is not numerical" in {
        a[WrongColumnTypeError] should be thrownBy {
          val action = new DatetimeComposer()
            .setTimestampColumns(Set(Year().setTimestampColumn(NameSingleColumnSelection("name"))))
            .setOutputColumn("timestamp")
          val schema = StructType(List(StructField("name", StringType)))
          action._transformSchema(schema)
        }
      }
    }
  }

  private def shouldComposeTimestamp(dataFrame: DataFrame, expectedDataFrame: DataFrame, outputName: String) = {
    val action = actionWithParametersSet(outputName)
    val deserialized = action.loadSerializedTransformer(tempDir)

    val resultDataFrame = action._transform(executionContext, dataFrame)
    assertDataFramesEqual(resultDataFrame, expectedDataFrame)

    val deserializedResultDataFrame = deserialized._transform(executionContext, dataFrame)
    assertDataFramesEqual(deserializedResultDataFrame, expectedDataFrame)
  }

  private def createComposedTimestampRow(schema: StructType, t: DateTime): Row =
    new GenericRowWithSchema(
      Array(
        t.getYear,
        t.getMonthOfYear,
        t.getDayOfMonth,
        t.getHourOfDay,
        t.getMinuteOfHour,
        t.getSecondOfMinute,
        new Timestamp(t.getMillis)
      ),
      schema
    )

  private def createUncomposedTimestampRow(schema: StructType, t: DateTime): Row =
    new GenericRowWithSchema(
      Array(t.getYear, t.getMonthOfYear, t.getDayOfMonth, t.getHourOfDay, t.getMinuteOfHour, t.getSecondOfMinute),
      schema
    )

  private def createSchema: StructType =
    StructType(orderedTimestampParts.map(p => StructField(p.name, IntegerType)))

  private def resultSchema(originalSchema: StructType, outputName: String): StructType =
    StructType(originalSchema.fields :+ StructField(outputName, TimestampType))

  private def actionWithParametersSet(outputName: String): DatetimeComposer = {
    new DatetimeComposer()
      .setTimestampColumns(
        orderedTimestampParts
          .map(p => p.setTimestampColumn(NameSingleColumnSelection(p.name)))
          .toSet
      )
      .setOutputColumn(outputName)
  }

  private def composeHour(dataFrame: DataFrame, outputName: String) = {
    new DatetimeComposer()
      .setTimestampColumns(Set(Hour().setTimestampColumn(NameSingleColumnSelection(Hour().name))))
      .setOutputColumn(outputName)
      ._transform(executionContext, dataFrame)
  }
}
