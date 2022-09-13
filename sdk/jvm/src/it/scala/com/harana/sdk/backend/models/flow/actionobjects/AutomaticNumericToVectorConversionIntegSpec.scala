package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow._
import com.harana.sdk.backend.models.flow.IntegratedTestSupport
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoices.SingleColumnChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers.{DiscreteCosineTransformer, Normalizer, PolynomialExpander, TransformerSerialization}
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection
import com.harana.spark.Linalg.Vectors
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._
import org.joda.time.DateTime
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import java.sql.Timestamp

class AutomaticNumericToVectorConversionIntegSpec
    extends IntegratedTestSupport
    with ScalaCheckDrivenPropertyChecks
    with Matchers
    with TransformerSerialization {

  val columns = Seq(
    StructField("c", DoubleType),
    StructField("b", StringType),
    StructField("a", new com.harana.spark.Linalg.VectorUDT()),
    StructField("x", TimestampType),
    StructField("z", BooleanType)
  )

  def schema: StructType = StructType(columns)

  //            "c"/0 "b"/1   "a"/2                "x"/3                                  "z"/4
  val row1 = Seq(1.1, "str1", Vectors.dense(10.0), new Timestamp(DateTime.now.getMillis), true)
  val row2 = Seq(2.2, "str2", Vectors.dense(20.0), new Timestamp(DateTime.now.getMillis), false)
  val row3 = Seq(3.3, "str3", Vectors.dense(30.0), new Timestamp(DateTime.now.getMillis), false)
  val data = Seq(row1) // , row2, row3)
  val dataFrame = createDataFrame(data.map(Row.fromSeq), schema)

  val noInPlace = NoInPlaceChoice().setOutputColumn("transformed")
  val singleNoInPlace = SingleColumnChoice().setInputColumn(NameSingleColumnSelection("c")).setInPlaceChoice(noInPlace)
  val singleInPlace = SingleColumnChoice().setInputColumn(NameSingleColumnSelection("c"))

  private def expectedInPlaceSchema(outputDataType: DataType) = schema
    .copy(schema.fields.updated(0, StructField("c", outputDataType, nullable = false)))

  private def expectedNoInPlaceSchema(outputDataType: DataType) = schema
    .copy(schema.fields.updated(0, StructField("c", DoubleType, nullable = schema("c").nullable)))
    .add(StructField("transformed", outputDataType, nullable = false))

  "Normalizer" should {
    val transformer = new Normalizer()
    transformer.set(transformer.pParameter, 1.0)

    "work correctly on double type column in noInPlace mode" in {
      transformer.setSingleOrMultiChoice(singleNoInPlace)
      val transformed = transformer._transform(executionContext, dataFrame)
      transformed.schema.get.treeString shouldBe expectedNoInPlaceSchema(DoubleType).treeString
    }

    "work correctly on double type column in inPlace mode" in {
      transformer.setSingleOrMultiChoice(singleInPlace)
      val transformed = transformer._transform(executionContext, dataFrame)
      transformed.schema.get.treeString shouldBe expectedInPlaceSchema(DoubleType).treeString
    }
  }

  "DiscreteCosineTransformer" should {
    val transformer = new DiscreteCosineTransformer()
    transformer.set(transformer.inverseParameter, false)

    "work correctly on double type column in noInPlace mode" in {
      transformer.setSingleOrMultiChoice(singleNoInPlace)
      val transformed = transformer._transform(executionContext, dataFrame)
      transformed.schema.get.treeString shouldBe expectedNoInPlaceSchema(DoubleType).treeString
    }

    "work correctly on double type column in inPlace mode" in {
      transformer.setSingleOrMultiChoice(singleInPlace)
      val transformed = transformer._transform(executionContext, dataFrame)
      transformed.schema.get.treeString shouldBe expectedInPlaceSchema(DoubleType).treeString
    }
  }

  "PolynomialExpander" should {
    val transformer = new PolynomialExpander()
    transformer.set(transformer.degreeParameter, 3.0)

    "work correctly on double type column in noInPlace mode" in {
      transformer.setSingleOrMultiChoice(singleNoInPlace)
      val transformed = transformer._transform(executionContext, dataFrame)
      transformed.schema.get.treeString shouldBe
        expectedNoInPlaceSchema(new com.harana.spark.Linalg.VectorUDT).treeString
    }

    "work correctly on double type column in inPlace mode" in {
      transformer.setSingleOrMultiChoice(singleInPlace)
      val transformed = transformer._transform(executionContext, dataFrame)
      transformed.schema.get.treeString shouldBe
        expectedInPlaceSchema(new com.harana.spark.Linalg.VectorUDT).treeString
    }
  }
}
