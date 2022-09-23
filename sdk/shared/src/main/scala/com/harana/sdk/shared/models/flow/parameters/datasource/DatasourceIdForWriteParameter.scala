package com.harana.sdk.shared.models.flow.parameters.datasource

import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterType}
import com.harana.sdk.shared.models.flow.parameters.ParameterType.DatasourceIdForWrite

import java.util.UUID

case class DatasourceIdForWriteParameter(name: String,
                                         required: Boolean = false,
                                         default: Option[UUID] = None) extends Parameter[UUID] {

  override def replicate(name: String) = copy(name = name)

  val parameterType = DatasourceIdForWrite

}
