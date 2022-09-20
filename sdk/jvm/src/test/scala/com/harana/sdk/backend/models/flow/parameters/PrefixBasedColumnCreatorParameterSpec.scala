package com.harana.sdk.backend.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.PrefixBasedColumnCreatorParameter
import io.circe.Json
import io.circe.syntax.EncoderOps


class PrefixBasedColumnCreatorParameterSpec extends AbstractParameterSpec[String, PrefixBasedColumnCreatorParameter] {

  def className = "PrefixBasedColumnCreatorParameter"

  def paramFixture: (PrefixBasedColumnCreatorParameter, Json) = {
    val description  = "Prefix based column creator description"
    val param        = PrefixBasedColumnCreatorParameter(
      name = "Prefix based column creator name",
    )
    val expectedJson = Map(
      "type"        -> Json.fromString("prefixBasedCreator"),
      "name"        -> Json.fromString(param.name),
      "description" -> Json.fromString(description),
      "isGriddable" -> Json.False,
      "default"     -> Json.Null
    )
    (param, expectedJson.asJson)
  }

  def valueFixture: (String, Json) = {
    val value = "abc"
    (value, Json.fromString(value))
  }
}
