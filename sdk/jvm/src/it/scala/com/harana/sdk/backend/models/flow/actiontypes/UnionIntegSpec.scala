package com.harana.sdk.backend.models.flow.actiontypes

import org.apache.spark.sql.Row
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType
import com.harana.sdk.backend.models.flow.{IntegratedTestSupport, Knowledge}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actiontypes.exceptions.SchemaMismatchError
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}

class UnionIntegSpec extends IntegratedTestSupport {

  import IntegratedTestSupport._

  val schema1 = StructType(List(StructField("column1", DoubleType), StructField("column2", DoubleType)))
  val rows1_1 = Seq(Row(1.0, 2.0), Row(2.0, 3.0))

  "Union" should {
    "return a union of two DataFrames" in {
      val rows1_2 = Seq(Row(2.0, 4.0), Row(4.0, 6.0))
      val df1 = createDataFrame(rows1_1, schema1)
      val df2 = createDataFrame(rows1_2, schema1)

      val merged = Union().executeUntyped(List(df1, df2))(executionContext).head.asInstanceOf[DataFrame]
      assertDataFramesEqual(merged, createDataFrame(rows1_1 ++ rows1_2, schema1))
    }

    "throw for mismatching types in DataFrames" in {
      val schema2 = StructType(List(StructField("column1", StringType), StructField("column2", DoubleType)))

      val rows2_1 = Seq(Row("a", 1.0), Row("b", 1.0))
      val df1 = createDataFrame(rows1_1, schema1)
      val df2 = createDataFrame(rows2_1, schema2)

      a[SchemaMismatchError] should be thrownBy {
        Union().executeUntyped(List(df1, df2))(executionContext)
      }
    }

    "throw for mismatching column names in DataFrames" in {
      val schema2 =
        StructType(List(StructField("column1", DoubleType), StructField("different_column_name", DoubleType)))

      val rows2_1 = Seq(Row(1.1, 1.0), Row(1.1, 1.0))
      val df1 = createDataFrame(rows1_1, schema1)
      val df2 = createDataFrame(rows2_1, schema2)

      a[SchemaMismatchError] should be thrownBy {
        Union().executeUntyped(List(df1, df2))(executionContext)
      }
    }
  }

  it should {
    "propagate schema when both schemas match" in {
      val structType = StructType(Seq(StructField("x", DoubleType), StructField("y", DoubleType)))
      val knowledgeDF1 = Knowledge(DataFrame.forInference(structType))
      val knowledgeDF2 = Knowledge(DataFrame.forInference(structType))
      Union().inferKnowledgeUntyped(List(knowledgeDF1, knowledgeDF2))(mock[InferContext]) shouldBe(List(knowledgeDF1), InferenceWarnings())
    }
    "generate error when schemas don't match" in {
      val structType1 = StructType(Seq(StructField("x", DoubleType)))
      val structType2 = StructType(Seq(StructField("y", DoubleType)))
      val knowledgeDF1 = Knowledge(DataFrame.forInference(structType1))
      val knowledgeDF2 = Knowledge(DataFrame.forInference(structType2))
      an[SchemaMismatchError] shouldBe thrownBy(
        Union().inferKnowledgeUntyped(List(knowledgeDF1, knowledgeDF2))(mock[InferContext])
      )
    }
  }
}