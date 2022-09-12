package com.harana.sdk.shared.models.flow.parameters.datasource

import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterType}

import java.util.UUID

case class DatasourceIdForReadParameter(name: String, description: Option[String]) extends Parameter[UUID] {

  override def replicate(name: String) = copy(name = name)

  val parameterType: ParameterType = ParameterType.DatasourceIdForRead

}
