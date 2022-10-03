package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.IntegratedTestSupport
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers.TransformerSerialization
import com.harana.sdk.backend.models.flow.actiontypes.exceptions.ColumnDoesNotExistError
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoices.SingleColumnChoice
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection
import com.harana.spark.Linalg.Vectors
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class GetFromVectorTransformerIntegSpec
    extends IntegratedTestSupport
    with ScalaCheckDrivenPropertyChecks
    with Matchers
    with TransformerSerialization {

  import TransformerSerialization._
  import com.harana.sdk.backend.models.flow.IntegratedTestSupport._

  val columns = Seq(StructField("id", IntegerType), StructField("data", new com.harana.spark.Linalg.VectorUDT()))

  def schema: StructType = StructType(columns)

  //         "id"/0  "a"/1
  val row1 = Seq(1, Vectors.dense(1.0, 10.0, 100.0))
  val row2 = Seq(2, Vectors.sparse(3, Seq((0, 2.0), (1, 20.0), (2, 200.0))))
  val row3 = Seq(3, null)
  val data = Seq(row1, row2, row3)
  val dataFrame = createDataFrame(data.map(Row.fromSeq), schema)

  "GetFromVectorTransformer" should {
    val expectedSchema = StructType(Seq(StructField("id", IntegerType), StructField("data", DoubleType)))
    val transformer    = new GetFromVectorTransformer()
      .setIndex(1)
      .setSingleOrMultiChoice(SingleColumnChoice().setInputColumn(NameSingleColumnSelection("data")))

    "infer correct schema" in {
      val filteredSchema = transformer._transformSchema(schema)
      filteredSchema shouldBe Some(expectedSchema)
    }

    "select correctly data from vector" in {
      val transformed                      = transformer._transform(executionContext, dataFrame)
      val expectedData                     = data.map { r =>
        val vec = r(1)
        if (vec != null)
          Seq(r.head, vec.asInstanceOf[com.harana.spark.Linalg.Vector](1))
        else
          Seq(r.head, null)
      }
      val expectedDataFrame                = createDataFrame(expectedData.map(Row.fromSeq), expectedSchema)
      assertDataFramesEqual(transformed, expectedDataFrame)
      val projectedBySerializedTransformer = projectedUsingSerializedTransformer(transformer)
      assertDataFramesEqual(transformed, projectedBySerializedTransformer)
    }

    "throw an exception" when {

      "the selected column does not exist" when {
        val transformer = new GetFromVectorTransformer()
          .setIndex(1)
          .setSingleOrMultiChoice(
            SingleColumnChoice().setInputColumn(NameSingleColumnSelection("thisColumnDoesNotExist"))
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
    }
  }

  private def projectedUsingSerializedTransformer(transformer: Transformer) =
    transformer.loadSerializedTransformer(tempDir)._transform(executionContext, dataFrame)

}
