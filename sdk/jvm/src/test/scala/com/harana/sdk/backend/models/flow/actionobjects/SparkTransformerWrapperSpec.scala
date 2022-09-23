package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.inference.exceptions.TransformSchemaError
import com.harana.sdk.backend.models.flow.{ExecutionContext, TestSupport, UnitSpec}
import com.harana.sdk.shared.models.flow.parameters.DoubleParameter
import com.harana.spark.ML
import org.apache.spark.ml
import org.apache.spark.ml.param.{BooleanParam, DoubleParam, ParamMap}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame => SparkDataFrame}
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar

class SparkTransformerWrapperSpec extends UnitSpec with TestSupport {

  import SparkTransformerWrapperSpec._

  "SparkTransformerWrapper" should {
    "transform DataFrame" in {
      val sparkTransformerWrapper = ExampleSparkTransformerWrapper().setParamWrapper(paramValueToSet)

      val context = mock[ExecutionContext]
      val inputDataFrame = createDataFrame()
      sparkTransformerWrapper._transform(context, inputDataFrame) shouldBe DataFrame.fromSparkDataFrame(outputDataFrame)
    }

    "infer schema" in {
      val sparkTransformerWrapper = ExampleSparkTransformerWrapper().setParamWrapper(paramValueToSet)
      val inputSchema = createSchema()
      sparkTransformerWrapper._transformSchema(inputSchema) shouldBe Some(outputSchema)
    }

    "forward an exception thrown by transformSchema wrapped in DeepLangException" in {
      val inputSchema = createSchema()
      val wrapper = ExampleSparkTransformerWrapper().setParamWrapper(paramValueToSet)
      wrapper.sparkTransformer.setTransformSchemaShouldThrow(true)
      val e = intercept[TransformSchemaError] {
        wrapper._transformSchema(inputSchema)
      }
      e.exception shouldBe exceptionThrownByTransformSchema
    }
  }
}

object SparkTransformerWrapperSpec extends MockitoSugar {

  case class ExampleSparkTransformerWrapper() extends SparkTransformerWrapper[ParamValueCheckingTransformer] {

    val id = "test"

    val paramWrapper = DoubleParameter("name")
    setDefault(paramWrapper, 0.0)
    def setParamWrapper(value: Double): this.type = set(paramWrapper, value)

    val parameters = Left(Array(paramWrapper))

    override def report(extended: Boolean = true) = ???

  }

  class ParamValueCheckingTransformer extends ML.Transformer {

    def this(id: String) = this()

    val param = new DoubleParam("id", "name", "description")

    def transformDF(dataset: SparkDataFrame): SparkDataFrame = {
      require($(param) == paramValueToSet)
      outputDataFrame
    }

    val shouldTransformSchemaThrowParameter = new BooleanParam("id", "shouldThrow", "description")
    setDefault(shouldTransformSchemaThrowParameter, false)
    def setTransformSchemaShouldThrow(b: Boolean) = set(shouldTransformSchemaThrowParameter, b)
    def transformSchema(schema: StructType): StructType = {
      if ($(shouldTransformSchemaThrowParameter)) throw exceptionThrownByTransformSchema
      require($(param) == paramValueToSet)
      outputSchema
    }

    val uid = "id"

    def copy(extra: ParamMap): ml.Transformer = defaultCopy(extra)

  }

  val outputSchema = StructType(Seq())
  val outputDataFrame = mock[SparkDataFrame]
  when(outputDataFrame.schema).thenReturn(outputSchema)

  val paramValueToSet = 12.0

  val exceptionThrownByTransformSchema = new Exception("mock exception")

}
