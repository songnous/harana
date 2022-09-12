package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.designer.flow.TestSupport
import com.harana.sdk.backend.models.designer.flow.ExecutionContext
import com.harana.sdk.backend.models.designer.flow.UnitSpec
import com.harana.sdk.backend.models.flow.ExecutionContext

class SparkEstimatorWrapperSpec extends UnitSpec with TestSupport {

  import EstimatorModelWrappersFixtures._

  "SparkEstimatorWrapper" should {

    "fit a DataFrame" in {
      val wrapper = new ExampleSparkEstimatorWrapper().setNumericParamWrapper(paramValueToSet)
      val inputDataFrame = createDataFrame()
      val modelWrapper = wrapper._fit(mock[ExecutionContext], inputDataFrame)
      modelWrapper.sparkModel shouldBe fitModel
    }

    "infer knowledge when schema is provided" in {
      val wrapper = new ExampleSparkEstimatorWrapper().setNumericParamWrapper(paramValueToSet)
      val inferredModelWrapper = wrapper._fit_infer(Some(createSchema()))
      inferredModelWrapper.parentEstimator.serializableEstimator shouldBe wrapper.serializableEstimator
    }

    "infer knowledge when schema isn't provided" in {
      val wrapper = new ExampleSparkEstimatorWrapper()
      val inferredModelWrapper = wrapper._fit_infer(None)
      inferredModelWrapper.parentEstimator.serializableEstimator shouldBe wrapper.serializableEstimator
    }
  }
}
