package com.harana.sdk.backend.models.flow.parameters.datasource

import com.harana.sdk.shared.models.flow.parameters.datasource.DatasourceIdForWriteParameter
import java.util.UUID

class DatasourceIdForWriteParamSpec extends AbstractParameterSpec[UUID, DatasourceIdForWriteParameter] {

  def className = "DatasourceIdForWriteParameter"

  def paramFixture: (DatasourceIdForWriteParameter, Json) = {
    val param        = DatasourceIdForWriteParameter(name = "Ds for write parameter name")
    val expectedJson = Json(
      "type"        -> "datasourceIdForWrite",
      "name"        -> param.name,
      "description" -> "",
      "default"     -> Json.Null,
      "isGriddable" -> Json.False
    )
    (param, expectedJson)
  }

  def valueFixture: (UUID, Json) = {
    val value = UUID.randomUUID()
    (value, value.toString)
  }
}
