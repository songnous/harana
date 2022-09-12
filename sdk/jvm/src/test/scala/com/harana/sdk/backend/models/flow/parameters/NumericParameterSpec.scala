package com.harana.sdk.backend.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.NumericParameter
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import io.circe.Json
import io.circe.syntax.EncoderOps


class NumericParameterSpec extends AbstractParameterSpec[Double, NumericParameter] {

  def className = "NumericParameter"

  def paramFixture: (NumericParameter, Json) = {
    val description = "Numeric parameter description"
    val param  = NumericParameter(
      name = "Numeric parameter",
      description = Some(description),
      validator = RangeValidator(1.0, 3.0, endIncluded = false)
    )
    val json = Map(
      "type"        -> Json.fromString("numeric"),
      "name"        -> Json.fromString(param.name),
      "description" -> Json.fromString(description + param.constraints),
      "default"     -> Json.Null,
      "isGriddable" -> Json.True,
      "validator"   -> Map(
                        "type"          -> Json.fromString("range"),
                        "configuration" -> Map(
                                            "begin"         -> Json.fromDouble(1.0).get,
                                            "end"           -> Json.fromDouble(3.0).get,
                                            "beginIncluded" -> Json.True,
                                            "endIncluded"   -> Json.False
                                           ).asJson
                      ).asJson
    )
    (param, json.asJson)
  }

  def valueFixture: (Double, Json) = {
    val value = 2.5
    (value, Json.fromDouble(value).get)
  }
}