package com.harana.sdk.backend.models.flow.parameters

import com.harana.sdk.shared.models.designer.flow.parameters
import com.harana.sdk.shared.models.flow
import com.harana.sdk.shared.models.flow.parameters.StringParameter
import com.harana.sdk.shared.models.flow.parameters.validators.AcceptAllRegexValidator
import io.circe.Json
import io.circe.syntax.EncoderOps


class StringParameterSpec extends AbstractParameterSpec[String, StringParameter] {

  def className = "StringParameter"

  def paramFixture: (StringParameter, Json) = {
    val description  = "String parameter description"
    val param = flow.parameters.StringParameter(
      name = "String parameter name",
      description = Some(description),
      validator = new AcceptAllRegexValidator
    )
    val expectedJson = Map(
                          "type"        -> Json.fromString("string"),
                          "name"        -> Json.fromString(param.name),
                          "description" -> Json.fromString(description),
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
