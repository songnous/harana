package com.harana.sdk.backend.models.flow.parameters.library

import com.harana.sdk.backend.models.flow.parameters.AbstractParameterSpec
import com.harana.sdk.shared.models.flow.parameters.library.LoadFromLibraryParameter
import io.circe.Json
import io.circe.syntax.EncoderOps

class LoadFromLibraryParamSpec extends AbstractParameterSpec[String, LoadFromLibraryParameter] {

  def className = "LoadFromLibraryParameter"

  def paramFixture: (LoadFromLibraryParameter, Json) = {
    val description  = "Load parameter description"
    val param        = LoadFromLibraryParameter("Load parameter name", Some(description))
    val expectedJson = Map(
                          "type"        -> Json.fromString("loadFromLibrary"),
                          "name"        -> Json.fromString(param.name),
                          "description" -> Json.fromString(description),
                          "default"     -> Json.Null,
                          "isGriddable" -> Json.False
                        )
    (param, expectedJson.asJson)
  }

  def valueFixture: (String, Json) = {
    val value = "input abcdefghij"
    (value, Json.fromString(value))
  }
}
