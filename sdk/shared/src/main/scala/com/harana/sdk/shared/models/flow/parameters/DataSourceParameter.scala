package com.harana.sdk.shared.models.flow.parameters

import io.circe.Json
import io.circe.generic.JsonCodec

@JsonCodec
case class DataSourceParameter(name: String,
                               required: Boolean = false,
                               default: Option[Json] = None,
                               inputPort: Int) extends Parameter[Json] {

  val parameterType: ParameterType = ParameterType.DataSource

  override def replicate(name: String): DataSourceParameter = new DataSourceParameter(name, required, default, inputPort)

}