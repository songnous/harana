package com.harana.sdk.shared.models.flow.parameters

import io.circe.Json

class DataSourceParameter(val name: String,
                          val required: Boolean = false,
                          val inputPort: Int) extends Parameter[Json] {

  val parameterType: ParameterType = ParameterType.DataSource

  override def replicate(name: String): DataSourceParameter = new DataSourceParameter(name, required, inputPort)

}