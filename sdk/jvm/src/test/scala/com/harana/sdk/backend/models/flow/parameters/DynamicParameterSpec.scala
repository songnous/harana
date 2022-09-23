package com.harana.sdk.backend.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.DynamicParameter
import io.circe.Json
import io.circe.syntax.EncoderOps


class DynamicParameterSpec extends AbstractParameterSpec[Json, DynamicParameter] {

  def className = "DynamicParameter"

  def paramFixture: (DynamicParameter, Json) = {
      val param       = new DynamicParameter("Dynamic parameter name", inputPort = 4)
    val json        = Map(
                        "type"        -> Json.fromString("dynamic"),
                        "name"        -> Json.fromString(param.name),
                        "inputPort"   -> Json.fromInt(param.inputPort),
                        "isGriddable" -> Json.False,
                        "default"     -> Json.Null
                      )
    (param, json.asJson)
  }

  def valueFixture: (Json, Json) = {
    val anyJson = Map("a" -> Json.fromInt(3), "b" -> Json.fromString("c")).asJson
    (anyJson, anyJson)
  }

  it should {
    "skip Json.Null values" in {
      val (param, _)  = paramFixture
      val input       = Map("a" -> Json.fromInt(3), "b" -> Json.Null).asJson
      val expected    = Map("a" -> Json.fromInt(3)).asJson
      param.valueFromJson(input) shouldBe expected
    }
  }
}
