package com.harana.sdk.backend.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.BooleanParameter
import io.circe.Json
import io.circe.syntax.EncoderOps


class BooleanParameterSpec extends AbstractParameterSpec[Boolean, BooleanParameter] {

  def className = "BooleanParameter"

  def paramFixture: (BooleanParameter, Json) = {
    val description = "Boolean param description"
    val param       = BooleanParameter("Boolean param name", Some(description))
    val json        = Map(
                        "type"        -> Json.fromString("boolean"),
                        "name"        -> Json.fromString(param.name),
                        "description" -> Json.fromString(description),
                        "isGriddable" -> Json.False,
                        "default"     -> Json.Null
                      )
    (param, json.asJson)
  }

  def valueFixture: (Boolean, Json) = (true, Json.True)

}
