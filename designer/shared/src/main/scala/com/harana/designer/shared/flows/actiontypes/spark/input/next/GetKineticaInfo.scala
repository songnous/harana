package com.harana.designer.shared.flows.actiontypes.spark.input.next

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup, ParameterValue}
import com.harana.sdk.shared.models.designer.data.DataSourceTypes

class GetKineticaInfo extends InputActionTypeInfo {

  val tags = Set()
  val dataSourceType = DataSourceTypes.Kinetica

  // General
  val dataSourceParameter = Parameter.DataSource("data-source", dataSourceType, required = true)
  val schemaParameter = Parameter.String("schema", required = true)
  val tableParameter = Parameter.String("table", required = true)
  val generalGroup = ParameterGroup("general", List(dataSourceParameter, schemaParameter, tableParameter))

  // Advanced
  val initialOffsetParameter = Parameter.Long("initialOffset", default = Some(ParameterValue.Long(0)))
  val maximumResultsParameter = Parameter.Long("maximumResults", default = Some(ParameterValue.Long(-1)))
  val partitionsParameter = Parameter.Long("partitions", default = Some(ParameterValue.Long(4)))
  val appendNewColumnsParameter = Parameter.Boolean("appendNewColumns", default = Some(ParameterValue.Boolean(false)))
  val mapColumnsByNameParameter = Parameter.Boolean("mapColumnsByName", default = Some(ParameterValue.Boolean(false)))
  val advancedGroup = ParameterGroup("advanced", List(initialOffsetParameter, maximumResultsParameter, partitionsParameter, appendNewColumnsParameter, mapColumnsByNameParameter))

  val parameterGroups = List(generalGroup, advancedGroup)

}