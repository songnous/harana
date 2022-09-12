package com.harana.designer.shared.flows.actiontypes.spark.input.next

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup, ParameterValue}
import com.harana.sdk.shared.models.designer.data.DataSourceTypes

class GetZuoraInfo extends InputActionTypeInfo {

  val tags = Set()

  // General
  val dataSourceType = DataSourceTypes.Zuora
  val dataSourceParameter = Parameter.DataSource("data-source", dataSourceType, required = true)
  val queryParameter = Parameter.String("query", multiLine = true, required = true)
  val generalGroup = ParameterGroup("general", List(dataSourceParameter, queryParameter))

  // Advanced
  val pageSizeParameter = Parameter.Long("pageSize", default = Some(ParameterValue.Long(1000)))
  val schemaParameter = Parameter.String("schema")
  val advancedGroup = ParameterGroup("advanced", List(pageSizeParameter, schemaParameter))

  val parameterGroups = List(generalGroup, advancedGroup)

}