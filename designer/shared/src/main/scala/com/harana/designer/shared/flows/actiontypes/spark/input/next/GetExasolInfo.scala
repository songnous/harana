package com.harana.designer.shared.flows.actiontypes.spark.input.next

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup}
import com.harana.sdk.shared.models.designer.data.DataSourceTypes

class GetExasolInfo extends InputActionTypeInfo {

  val tags = Set()

  // General
  val dataSourceType = DataSourceTypes.Exasol
  val dataSourceParameter = Parameter.DataSource("data-source", dataSourceType, required = true)

  val parameterGroups = List()
}