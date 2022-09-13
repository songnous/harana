package com.harana.sdk.backend.models.flow.parameters.library

import com.harana.sdk.backend.models.flow.parameters.AbstractParameterSpec
import com.harana.sdk.shared.models.flow.parameters.library.SaveToLibraryParameter
import io.circe.Json
import io.circe.syntax.EncoderOps

class SaveToLibraryParamSpec extends AbstractParameterSpec[String, SaveToLibraryParameter] {

  def className = "SaveToLibraryParameter"

  def paramFixture: (SaveToLibraryParameter, Json) = {
    val description  = "Save parameter description"
    val param        = SaveToLibraryParameter("Save parameter name", Some(description))
    val expectedJson = Map(
                          "type"        -> Json.fromString("saveToLibrary"),
                          "name"        -> Json.fromString(param.name),
                          "description" -> Json.fromString(description),
                          "default"     -> Json.Null,
                          "isGriddable" -> Json.False
                        )
    (param, expectedJson.asJson)
  }

  def valueFixture: (String, Json) = {
    val value = "output abcdefghij"
    (value, Json.fromString(value))
  }
}
