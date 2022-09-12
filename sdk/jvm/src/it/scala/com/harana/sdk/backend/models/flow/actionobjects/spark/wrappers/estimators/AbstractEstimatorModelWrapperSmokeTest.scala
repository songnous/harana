package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import org.apache.spark.sql.types.StructType
import com.harana.sdk.backend.models.flow.IntegratedTestSupport.{assertDataFramesEqual, assertSchemaEqual}
import AbstractEstimatorModelWrapperSmokeTest.TestDataFrameRow
import com.harana.sdk.backend.models.flow.IntegratedTestSupport
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actionobjects.{Estimator, Transformer}
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers.TransformerSerialization
import com.harana.sdk.shared.models.flow.parameters.ParameterPair
import com.harana.spark.Linalg
import com.harana.spark.Linalg.Vectors

abstract class AbstractEstimatorModelWrapperSmokeTest extends IntegratedTestSupport with TransformerSerialization {

  import TransformerSerialization._

  def className: String

  val estimator: Estimator[Transformer]
  val estimatorParameters: Seq[ParameterPair[_]]

  val dataFrame = {
    val rowSeq = Seq(
      TestDataFrameRow(
        0.0,
        0.5,
        Vectors.dense(1.0, 2.0, 3.0),
        0,
        0,
        0.2,
        1.0,
        0.5,
        Seq("a", "a", "a", "b", "b", "c").toArray,
        Vectors.dense(0.3, 0.5, 1)
      ),
      TestDataFrameRow(
        1.0,
        2.0,
        Vectors.dense(4.0, 5.0, 6.0),
        1,
        1,
        0.4,
        0.0,
        0.2,
        Seq("a", "b", "c", "d", "d", "d").toArray,
        Vectors.dense(0.2, 0.1, 0.5)
      ),
      TestDataFrameRow(
        1.0,
        0.0,
        Vectors.dense(16.0, 11.0, 9.0),
        2,
        3,
        0.4,
        1.0,
        0.8,
        Seq("a", "c", "d", "f", "f", "g").toArray,
        Vectors.dense(0.2, 0.1, 1)
      ),
      TestDataFrameRow(
        0.0,
        1.0,
        Vectors.dense(32.0, 11.0, 9.0),
        4,
        3,
        0.2,
        0.0,
        0.1,
        Seq("b", "d", "d", "f", "f", "g").toArray,
        Vectors.dense(0.1, 0.2, 0.1)
      )
    )
    createDataFrame(rowSeq)
  }

  def assertTransformedDF(dataFrame: DataFrame) = {}

  def assertTransformedSchema(schema: StructType) = {}

  def isAlgorithmDeterministic: Boolean = true

  className should {
    "successfully run _fit(), _transform() and _transformSchema()" in {
      val estimatorWithParameters = estimator.set(estimatorParameters: _*)
      val transformer         = estimatorWithParameters._fit(executionContext, dataFrame)
      val transformed         = transformer._transform(executionContext, dataFrame)
      assertTransformedDF(transformed)
      val transformedSchema   = transformer._transformSchema(dataFrame.sparkDataFrame.schema)
      assertTransformedSchema(transformedSchema.get)
      testSerializedTransformer(transformer, transformed, transformedSchema.get)
    }
    "successfully run _fit_infer() and _transformSchema() with schema" in {
      val estimatorWithParameters = estimator.set(estimatorParameters: _*)
      val transformer         = estimatorWithParameters._fit_infer(Some(dataFrame.sparkDataFrame.schema))
      transformer._transformSchema(dataFrame.sparkDataFrame.schema)
    }
    "successfully run _fit_infer() without schema" in {
      val estimatorWithParameters = estimator.set(estimatorParameters: _*)
      estimatorWithParameters._fit_infer(None)
    }
    "successfully run report" in {
      val estimatorWithParameters = estimator.set(estimatorParameters: _*)
      estimatorWithParameters.report()
    }
  }

  def testSerializedTransformer(transformer: Transformer, expectedDF: DataFrame, expectedSchema: StructType) = {

    val (df, Some(schema)) = useSerializedTransformer(transformer, dataFrame)
    assertTransformedDF(df)
    if (isAlgorithmDeterministic) assertDataFramesEqual(expectedDF, df, checkRowOrder = false)
    assertTransformedSchema(schema)
    assertSchemaEqual(schema, expectedSchema)
  }

  def useSerializedTransformer(transformer: Transformer, dataFrame: DataFrame): (DataFrame, Option[StructType]) = {
    transformer.parameterValuesToJson
    checkTransformerCorrectness(transformer)
    val deserialized = transformer.loadSerializedTransformer(tempDir)
    val resultDF     = deserialized.transform.apply(executionContext)(())(dataFrame)
    val resultSchema = deserialized._transformSchema(dataFrame.sparkDataFrame.schema)
    checkTransformerCorrectness(deserialized)
    (resultDF, resultSchema)
  }

  private def checkTransformerCorrectness(transformer: Transformer) =
    transformer.report()
}

object AbstractEstimatorModelWrapperSmokeTest {

  case class TestDataFrameRow(
      myLabel: Double,
      myWeight: Double,
      myFeatures: Linalg.Vector,
      myItemId: Int,
      myUserId: Int,
      myRating: Double,
      myCensor: Double,
      myNoZeroLabel: Double,
      myStringFeatures: Array[String],
      myStandardizedFeatures: Linalg.Vector
  )
}