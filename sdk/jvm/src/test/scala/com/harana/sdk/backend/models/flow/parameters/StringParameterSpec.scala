package com.harana.sdk.backend.models.flow.parameters

import com.harana.sdk.shared.models.flow
import com.harana.sdk.shared.models.flow.parameters.StringParameter
import com.harana.sdk.shared.models.flow.parameters.validators.RegexValidator
import io.circe.Json
import io.circe.syntax.EncoderOps


class StringParameterSpec extends AbstractParameterSpec[String, StringParameter] {

  def className = "StringParameter"

  def paramFixture: (StringParameter, Json) = {
    val param = flow.parameters.StringParameter("parameter-name", RegexValidator.AcceptAll)
    val expectedJson = Map(
                          "type"        -> Json.fromString("string"),
                          "name"        -> Json.fromString(param.name),
                          "default"     -> Json.Null,
                          "isGriddable" -> Json.False,
                          "validator"   -> Map(
                                            "type" -> Json.fromString("regex"),
                                            "configuration" -> Map("regex" -> ".*").asJson
                                           ).asJson
                        )
    (param, expectedJson.asJson)
  }

  def valueFixture: (String, Json) = {
    val value = "abcdefghij"
    (value, Json.fromString(value))
  }
}
