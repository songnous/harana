package com.harana.sdk.backend.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.ColumnSelectorParameter
import com.harana.sdk.shared.models.flow.parameters.selections.{ColumnSelection, MultipleColumnSelection, NameColumnSelection}
import io.circe.Json
import io.circe.syntax.EncoderOps

class ColumnSelectorParameterSpec extends AbstractParameterSpec[MultipleColumnSelection, ColumnSelectorParameter] {

  def className = "MultipleColumnCreatorParameter"

  def paramFixture: (ColumnSelectorParameter, Json) = {
    val description  = "Column selector description"
    val param        = ColumnSelectorParameter(name = "Column selector name", portIndex = 0)
    val expectedJson = Map(
                          "type"        -> Json.fromString("selector"),
                          "name"        -> Json.fromString(param.name),
                          "description" -> Json.fromString(description),
                          "portIndex"   -> Json.fromInt(param.portIndex),
                          "isSingle"    -> Json.False,
                          "isGriddable" -> Json.False,
                          "default"     -> Json.Null
                        )
    (param, expectedJson.asJson)
  }

  def valueFixture: (MultipleColumnSelection, Json) = {
    val value = MultipleColumnSelection(selections = Vector[ColumnSelection](NameColumnSelection(Set("a", "b"))))
    val expectedJson = Map(
                        "selections" -> Seq(Map(
                            "type"    -> Json.fromString("columnList"),
                            "values"  -> Seq("a", "b").asJson
                          )).asJson,
                        "excluding"  -> Json.False
                      )
    (value, expectedJson.asJson)
  }
}
