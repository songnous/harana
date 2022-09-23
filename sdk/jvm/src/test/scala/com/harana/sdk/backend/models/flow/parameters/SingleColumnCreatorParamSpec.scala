package com.harana.sdk.backend.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.SingleColumnCreatorParameter
import com.harana.sdk.shared.models.flow.parameters.validators.{ColumnNameValidator, ColumnValidator}
import io.circe.Json
import io.circe.syntax.EncoderOps


class SingleColumnCreatorParamSpec extends AbstractParameterSpec[String, SingleColumnCreatorParameter] {

  def className = "SingleColumnCreatorParameter"

  def paramFixture: (SingleColumnCreatorParameter, Json) = {
    val param        = SingleColumnCreatorParameter("Single column creator name")
    val expectedJson = Map(
                          "type"        -> Json.fromString("creator"),
                          "name"        -> Json.fromString(param.name),
                          "isGriddable" -> Json.False,
                          "default"     -> Json.Null,
                          "validator"   -> Map(
                                              "type"          -> Json.fromString("regex"),
                                              "configuration" -> Map("regex" -> ColumnValidator.Name.toString).asJson
                                            ).asJson
                        )
    (param, expectedJson.asJson)
  }

  def valueFixture: (String, Json) = {
    val value = "abc"
    (value, Json.fromString(value))
  }
}
