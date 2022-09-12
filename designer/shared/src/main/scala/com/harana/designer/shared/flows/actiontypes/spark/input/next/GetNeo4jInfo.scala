package com.harana.designer.shared.flows.actiontypes.spark.input.next

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup}
import com.harana.sdk.shared.models.designer.data.DataSourceTypes

class GetNeo4jInfo extends InputActionTypeInfo {

  val tags = Set()
  val dataSourceType = DataSourceTypes.Neo4j
  val dataSourceParameter = Parameter.DataSource("data-source", dataSourceType, required = true)
  val parameterGroups = List(ParameterGroup("general", List(dataSourceParameter)))

}