package com.harana.sdk.backend.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.MultipleColumnCreatorParameter
import io.circe.Json
import io.circe.syntax.EncoderOps

class MultipleColumnCreatorParameterSpec extends AbstractParameterSpec[Array[String], MultipleColumnCreatorParameter] {

  def className = "MultipleColumnCreatorParameter"

  def paramFixture: (MultipleColumnCreatorParameter, Json) = {
    val description  = "Multiple column creator description"
    val param        = MultipleColumnCreatorParameter(
      name = "Multiple column creator name",
      description = Some(description)
    )
    val expectedJson = Map(
                          "type"        -> Json.fromString("multipleCreator"),
                          "name"        -> Json.fromString(param.name),
                          "description" -> Json.fromString(description),
                          "isGriddable" -> Json.False,
                          "default"     -> Json.Null
                        )
    (param, expectedJson.asJson)
  }

  def valueFixture: (Array[String], Json) = {
    val value = Array("a", "b", "c")
    (value, value.asJson)
  }
}