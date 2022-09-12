package com.harana.sdk.backend.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.Parameter
import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import io.circe.Json

abstract class AbstractParameterSpec[T, U <: Parameter[T]] extends AnyWordSpec with Matchers with MockitoSugar {

  def className: String

  def paramFixture: (U, Json) // param + its json description

  def valueFixture: (T, Json) // value + its json description

  val defaultValue: T = valueFixture._1

  def serializeDefaultValue(default: T) = paramFixture._1.valueToJson(default)

  className should {
    "serialize itself to JSON" when {
      "default value is not provided" in {
        val (param, expectedJson) = paramFixture
        param.asJson(maybeDefault = None) shouldBe expectedJson
      }
      "default value is provided" in {
        val (param, expectedJson) = paramFixture
        val expectedJsonWithDefault = Json(
          expectedJson.asJson.fields + ("default" -> serializeDefaultValue(defaultValue))
        )
        param.asJson(maybeDefault = Some(defaultValue)) shouldBe expectedJsonWithDefault
      }
    }
  }

  it should {
    "serialize value to JSON" in {
      val param = paramFixture._1
      val (value, expectedJson) = valueFixture
      param.valueToJson(value) shouldBe expectedJson
    }
  }

  it should {
    "deserialize value from JSON" in {
      val param = paramFixture._1
      val (expectedValue, valueJson) = valueFixture
      val extractedValue = param.valueFromJson(valueJson)
      extractedValue shouldBe expectedValue
    }
  }
}
