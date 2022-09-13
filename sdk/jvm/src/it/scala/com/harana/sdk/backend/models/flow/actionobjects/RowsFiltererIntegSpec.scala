package com.harana.sdk.backend.models.flow.actionobjects

import org.apache.spark.sql.Row
import org.apache.spark.sql.types._
import org.scalatest.matchers.should.Matchers
import com.harana.sdk.backend.models.flow._
import com.harana.sdk.backend.models.flow.IntegratedTestSupport
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers.TransformerSerialization
import org.scalatest.matchers.should.Matchers

class RowsFiltererIntegSpec extends IntegratedTestSupport with Matchers with TransformerSerialization {

  import com.harana.sdk.backend.models.flow.IntegratedTestSupport._
  import TransformerSerialization._

  val columns = Seq(StructField("a", DoubleType), StructField("b", StringType), StructField("c", BooleanType))
  def schema: StructType = StructType(columns)

  val row1 = Seq(1.0, "aaa", true)
  val row2 = Seq(2.0, "b", false)
  val row3 = Seq(3.3, "cc", true)
  val data = Seq(row1, row2, row3)

  "RowsFilterer" should {

    "select correct rows based on the condition" in {
      val filterer = new RowsFilterer().setCondition("a > 1 AND c = TRUE")

      val dataFrame = createDataFrame(data.map(Row.fromSeq), schema)
      val result = filterer.applyTransformationAndSerialization(tempDir, dataFrame)
      val expectedDataFrame = createDataFrame(Seq(row3).map(Row.fromSeq), schema)
      assertDataFramesEqual(result, expectedDataFrame)
    }

    "infer correct schema" in {
      val filterer = new RowsFilterer().setCondition("a > 1")
      filterer._transformSchema(schema) shouldBe Some(schema)
    }
  }
}
