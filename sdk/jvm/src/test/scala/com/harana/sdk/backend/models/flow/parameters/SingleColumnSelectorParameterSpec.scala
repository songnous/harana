package com.harana.sdk.backend.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.SingleColumnSelectorParameter
import com.harana.sdk.shared.models.flow.parameters.selections.{IndexSingleColumnSelection, SingleColumnSelection}
import io.circe.Json
import io.circe.syntax.EncoderOps

class SingleColumnSelectorParameterSpec extends AbstractParameterSpec[SingleColumnSelection, SingleColumnSelectorParameter] {

  def className = "SingleColumnSelectorParameter"

  def paramFixture: (SingleColumnSelectorParameter, Json) = {
    val description  = "Single column selector description"
    val param        =
      SingleColumnSelectorParameter(name = "Single column selector name", portIndex = 0)
    val expectedJson = Map(
                          "type"        -> Json.fromString("selector"),
                          "name"        -> Json.fromString(param.name),
                          "description" -> Json.fromString(description),
                          "portIndex"   -> Json.fromInt(param.portIndex),
                          "isSingle"    -> Json.True,
                          "isGriddable" -> Json.False,
                          "default"     -> Json.Null
                        )
    (param, expectedJson.asJson)
  }

  def valueFixture: (SingleColumnSelection, Json) = {
    val selection    = IndexSingleColumnSelection(2)
    val expectedJson = Map(
      "type"  -> Json.fromString("index"),
      "value" -> Json.fromInt(2)
    )
    (selection, expectedJson.asJson)
  }
}
