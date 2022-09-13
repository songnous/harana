package com.harana.sdk.backend.models.flow.actionobjects

import java.sql.Timestamp
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._
import org.joda.time.DateTime
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import com.harana.sdk.backend.models.flow._
import com.harana.sdk.backend.models.flow.actionobjects.Projector.ColumnProjection
import com.harana.sdk.backend.models.flow.actionobjects.Projector.RenameColumnChoice.Yes
import com.harana.sdk.backend.models.flow.IntegratedTestSupport
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers.TransformerSerialization
import com.harana.sdk.backend.models.flow.actions.exceptions.{ColumnDoesNotExistError, DuplicatedColumnsError}
import com.harana.sdk.shared.models.flow.parameters.selections.{IndexSingleColumnSelection, NameSingleColumnSelection}
import org.scalatest.matchers.should.Matchers

class ProjectorIntegSpec
    extends IntegratedTestSupport
    with ScalaCheckDrivenPropertyChecks
    with Matchers
    with TransformerSerialization {

  import com.harana.sdk.backend.models.flow.IntegratedTestSupport._
  import TransformerSerialization._

  val specialCharactersName = "a'a-z"

  val columns = Seq(
    StructField("c", IntegerType),
    StructField("b", StringType),
    StructField(specialCharactersName, DoubleType),
    StructField("x", TimestampType),
    StructField("z", BooleanType)
  )

  def schema: StructType = StructType(columns)

  //         "c"/0  "b"/1   "a"/2 "x"/3                                  "z"/4
  val row1 = Seq(1, "str1", 10.0, new Timestamp(DateTime.now.getMillis), true)
  val row2 = Seq(2, "str2", 20.0, new Timestamp(DateTime.now.getMillis), false)
  val row3 = Seq(3, "str3", 30.0, new Timestamp(DateTime.now.getMillis), false)

  val data = Seq(row1, row2, row3)

  val dataFrame = createDataFrame(data.map(Row.fromSeq), schema)

  "Projector" should {
    val expectedSchema = StructType(
      Seq(StructField(specialCharactersName, DoubleType), StructField(s"renamed_$specialCharactersName", DoubleType))
    )
    val transformer    = new Projector().setProjectionColumns(
      Seq(
        ColumnProjection().setOriginalColumn(NameSingleColumnSelection(specialCharactersName)),
        ColumnProjection()
          .setOriginalColumn(NameSingleColumnSelection(specialCharactersName))
          .setRenameColumn(new Yes().setColumnName(s"renamed_$specialCharactersName"))
      )
    )

    "select correctly the same column multiple times" in {
      val projected                        = transformer._transform(executionContext, dataFrame)
      val expectedData                     = data.map(r => Seq(r(2), r(2)))
      val expectedDataFrame                = createDataFrame(expectedData.map(Row.fromSeq), expectedSchema)
      assertDataFramesEqual(projected, expectedDataFrame)
      val projectedBySerializedTransformer = projectedUsingSerializedTransformer(transformer)
      assertDataFramesEqual(projected, projectedBySerializedTransformer)
    }
    "infer correct schema" in {
      val filteredSchema = transformer._transformSchema(schema)
      filteredSchema shouldBe Some(expectedSchema)
    }
    "throw an exception" when {
      "the columns selected by name does not exist" when {
        val transformer = new Projector().setProjectionColumns(
          Seq(
            ColumnProjection().setOriginalColumn(NameSingleColumnSelection("thisColumnDoesNotExist"))
          )
        )

        "transforming a DataFrame" in {
          intercept[ColumnDoesNotExistError] {
            transformer._transform(executionContext, dataFrame)
          }
        }

        "transforming a schema" in {
          intercept[ColumnDoesNotExistError] {
            transformer._transformSchema(schema)
          }
        }
      }

      "the columns selected by index does not exist" when {
        val transformer = new Projector().setProjectionColumns(
          Seq(
            ColumnProjection().setOriginalColumn(IndexSingleColumnSelection(1000))
          )
        )

        "transforming a DataFrame" in {
          intercept[ColumnDoesNotExistError] {
            transformer._transform(executionContext, dataFrame)
          }
        }

        "transforming a schema" in {
          intercept[ColumnDoesNotExistError] {
            transformer._transformSchema(schema)
          }
        }
      }

      "the output DataFrame has duplicated columns" when {
        val transformer = new Projector().setProjectionColumns(
          Seq(
            ColumnProjection()
              .setOriginalColumn(NameSingleColumnSelection(specialCharactersName))
              .setRenameColumn(new Yes().setColumnName("duplicatedName")),
            ColumnProjection()
              .setOriginalColumn(NameSingleColumnSelection("b"))
              .setRenameColumn(new Yes().setColumnName("duplicatedName"))
          )
        )

        "transforming a DataFrame" in {
          intercept[DuplicatedColumnsError] {
            transformer._transform(executionContext, dataFrame)
          }
        }
      }

      "the transformer uses output column name with backticks" when {
        val transformer = new Projector().setProjectionColumns(
          Seq(
            ColumnProjection()
              .setOriginalColumn(NameSingleColumnSelection(specialCharactersName))
              .setRenameColumn(new Yes().setColumnName("columnName")),
            ColumnProjection()
              .setOriginalColumn(NameSingleColumnSelection("b"))
              .setRenameColumn(new Yes().setColumnName("column`Name`With``Backticks`"))
          )
        )

        "transforming a DataFrame" in {
          intercept[Exception] {
            transformer._transform(executionContext, dataFrame)
          }
        }
      }
    }
  }

  it when {
    "selection is empty" should {
      val emptyProjector = new Projector().setProjectionColumns(Seq())
      "produce an empty DataFrame" in {
        val emptyDataFrame = emptyProjector._transform(executionContext, dataFrame)
        emptyDataFrame.sparkDataFrame.collectAsList() shouldBe empty
      }
      "produce an empty schema" in {
        val Some(inferredSchema) = emptyProjector._transformSchema(schema)
        inferredSchema.fields shouldBe empty
      }
    }
  }

  private def projectedUsingSerializedTransformer(transformer: Transformer) =
    transformer.loadSerializedTransformer(tempDir)._transform(executionContext, dataFrame)

}
