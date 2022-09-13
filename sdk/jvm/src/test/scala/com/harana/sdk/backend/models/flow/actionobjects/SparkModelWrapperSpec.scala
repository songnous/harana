package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actionobjects.serialization.SerializableSparkModel
import com.harana.sdk.backend.models.flow.TestSupport
import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.UnitSpec
import com.harana.sdk.backend.models.flow.inference.exceptions.TransformSchemaError
import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.inference.exceptions.TransformSchemaError
import com.harana.sdk.shared.models.flow.parameters.ParameterMap

class SparkModelWrapperSpec extends UnitSpec with TestSupport {

  import EstimatorModelWrappersFixtures._

  "SparkModelWrapper" should {

    "ignore default parameter values" in {
      val wrapper = new ExampleSparkModelWrapper()
      wrapper.extractParameterMap() shouldBe ParameterMap.empty
    }

    "transform a DataFrame" in {
      val wrapper = prepareWrapperWithParameters()
      wrapper._transform(mock[ExecutionContext], createDataFrame()) shouldBe
        DataFrame.fromSparkDataFrame(fitDataFrame)
    }

    "transform schema" in {
      val inputSchema = createSchema()
      val wrapper = prepareWrapperWithParameters()
      wrapper._transformSchema(inputSchema) shouldBe Some(transformedSchema)
    }

    "forward an exception thrown by transformSchema wrapped in DeepLangException" in {
      val inputSchema = createSchema()
      val wrapper = prepareWrapperWithParameters()
      wrapper.parentEstimator.sparkEstimator.setTransformSchemaShouldThrow(true)
      val e = intercept[TransformSchemaError] {
        wrapper._transformSchema(inputSchema)
      }
      e.shouldBe exceptionThrownByTransformSchema
    }
  }

  private def prepareWrapperWithParameters(): ExampleSparkModelWrapper = {
    val model = new SerializableSparkModel(new ExampleSparkModel())
    val wrapper = new ExampleSparkModelWrapper().setModel(model)
    val parentEstimator = new ExampleSparkEstimatorWrapper()
    wrapper.setParent(parentEstimator).setNumericParameter(paramValueToSet)
  }
}
