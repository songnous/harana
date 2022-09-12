package com.harana.designer.shared.flows.actiontypes.spark.input.next

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup}
import com.harana.sdk.shared.models.designer.data.DataSourceTypes

class GetMemSqlInfo extends InputActionTypeInfo {

  val tags = Set()
  val dataSourceType = DataSourceTypes.MemSql
  val dataSourceParameter = Parameter.DataSource("data-source", dataSourceType, required = true)
  val databaseParameter = Parameter.String("database", required = true)
  val tableParameter = Parameter.String("table", required = true)
  val parameterGroups = List(ParameterGroup("general", List(dataSourceParameter, databaseParameter, tableParameter)))

}