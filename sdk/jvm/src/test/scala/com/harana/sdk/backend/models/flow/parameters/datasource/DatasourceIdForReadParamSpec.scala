package com.harana.sdk.backend.models.flow.parameters.datasource

import com.harana.sdk.backend.models.designer.flow.parameters.AbstractParameterSpec
import com.harana.sdk.shared.models.flow.parameters.datasource.DatasourceIdForReadParameter
import io.circe.Json
import io.circe.syntax.EncoderOps

import java.util.UUID

class DatasourceIdForReadParamSpec extends AbstractParameterSpec[UUID, DatasourceIdForReadParameter] {

  def className = "DatasourceIdForReadParameter"

  def paramFixture: (DatasourceIdForReadParameter, Json) = {
    val param        = DatasourceIdForReadParameter(name = "Ds for read parameter name", description = None)
    val expectedJson = Map(
                          "type"        -> Json.fromString("datasourceIdForRead"),
                          "name"        -> Json.fromString(param.name),
                          "description" -> Json.fromString(""),
                          "default"     -> Json.Null,
                          "isGriddable" -> Json.False
                        )
    (param, expectedJson.asJson)
  }

  def valueFixture: (UUID, Json) = {
    val value = UUID.randomUUID()
    (value, Json.fromString(value.toString))
  }
}
