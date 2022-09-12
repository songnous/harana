package com.harana.designer.shared.flows.actiontypes.spark.input.next

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup}
import com.harana.sdk.shared.models.designer.data.DataSourceTypes

class GetMarketoInfo extends InputActionTypeInfo {

  val tags = Set()
  val dataSourceType = DataSourceTypes.Marketo

  // General
  val dataSourceParameter = Parameter.DataSource("data-source", dataSourceType, required = true)
  val objectParameter = Parameter.String("object")
  val generalGroup = ParameterGroup("general", List(dataSourceParameter, objectParameter))

  // Query
  val filterKeyParameter = Parameter.String("filterKey")
  val filterValuesParameter = Parameter.StringList("filterValues")
  val sinceDateTimeParameter = Parameter.String("sinceDateTime")
  val queryGroup = ParameterGroup("query", List(filterKeyParameter, filterValuesParameter, sinceDateTimeParameter))

  val parameterGroups = List(generalGroup, queryGroup)
}