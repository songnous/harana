package com.harana.sdk.backend.models.flow.parameters.wrappers.spark

import com.harana.sdk.shared.models.flow.parameters.{DoubleParameter, FloatParameter, IntParameter, Parameters}
import org.apache.spark.ml
import org.apache.spark.ml.param._
import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class WrappersDefaultValidationSpec extends AnyWordSpec with Matchers with MockitoSugar {

  class ExampleSparkParameters extends ml.param.Params {
    val uid = "id"

    val intSparkParameter = new IntParam("", "name", "description")
    val floatSparkParameter = new FloatParam("", "name", "description")
    val doubleSparkParameter = new DoubleParam("", "name", "description")

    def copy(extra: ParamMap): Params = ???
  }

  "IntParamWrapper" should {
    val intParamWrapper = IntParameter("name")

    "validate whole Int range" in {
      intParamWrapper.validate(Int.MinValue + 1) shouldBe empty
      intParamWrapper.validate(Int.MaxValue - 1) shouldBe empty
    }
    "reject fractional values" in {
      intParamWrapper.validate(Int.MinValue + 0.005) should have size 1
      intParamWrapper.validate(Int.MaxValue - 0.005) should have size 1
    }
  }

  "FloatParamWrapper" should {
    val floatParamWrapper = FloatParameter("name")

    "validate whole Float range" in {
      floatParamWrapper.validate(Float.MinValue + 1) shouldBe empty
      floatParamWrapper.validate(Float.MaxValue - 1) shouldBe empty
    }

    "reject values out of Float range" in {
      floatParamWrapper.validate(Double.MinValue + 1) should have size 1
      floatParamWrapper.validate(Double.MaxValue - 1) should have size 1
    }
  }

  "DoubleParamWrapper" should {

    "validate whole Double range" in {
      val doubleParamWrapper = new DoubleParameter("name")
      doubleParamWrapper.validate(Double.MinValue + 1) shouldBe empty
      doubleParamWrapper.validate(Double.MinValue - 1) shouldBe empty
    }
  }
}
