package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.sdk.backend.models.flow.IntegratedTestSupport
import com.harana.spark.Linalg.Vectors
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._
import com.harana.sdk.backend.models.flow.actionobjects.Transformer
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame

abstract class AbstractTransformerWrapperSmokeTest[+T <: Transformer] extends IntegratedTestSupport with TransformerSerialization {

  import IntegratedTestSupport._
  import TransformerSerialization._

  def transformerWithParameters: T

  def deserializedTransformer: Transformer =
    transformerWithParameters.loadSerializedTransformer(tempDir)

  final def className = transformerWithParameters.getClass.getSimpleName

  val inputDataFrameSchema = StructType(
    Seq(
      StructField("as", ArrayType(StringType, containsNull = true)),
      StructField("s", StringType),
      StructField("i", IntegerType),
      StructField("i2", IntegerType),
      StructField("d", DoubleType),
      StructField("v", new com.harana.spark.Linalg.VectorUDT)
    )
  )

  lazy val inputDataFrame = {
    val rowSeq = Seq(
      Row(Array("ala", "kot", "ala"), "aAa bBb cCc", 0, 0, 0.0, Vectors.dense(0.0, 0.0, 0.0)),
      Row(Array(null), "das99213 99721 8i!#@!", 1, 1, 1.0, Vectors.dense(1.0, 1.0, 1.0))
    )
    createDataFrame(rowSeq, inputDataFrameSchema)
  }

  className should {
    "successfully run _transform()" in {
      val transformed                        = transformerWithParameters._transform(executionContext, inputDataFrame)
      val transformedBySerializedTransformer =
        deserializedTransformer._transform(executionContext, inputDataFrame)
      assertDataFramesEqual(transformed, transformedBySerializedTransformer)
    }
    "successfully run _transformSchema()" in {
      val transformedSchema  =
        transformerWithParameters._transformSchema(inputDataFrame.sparkDataFrame.schema)
      val transformedSchema2 =
        deserializedTransformer._transformSchema(inputDataFrame.sparkDataFrame.schema)
      assertSchemaEqual(transformedSchema.get, transformedSchema2.get)
    }
    "succesfully run report" in {
      val report  = transformerWithParameters.report()
      val report2 = deserializedTransformer.report()
      report shouldBe report2
    }
  }
}
