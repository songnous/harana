package com.harana.designer.shared.flows.actiontypes.spark.input.next

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup}
import com.harana.sdk.shared.models.designer.data.DataSourceTypes

class GetGreenplumInfo extends InputActionTypeInfo {

  val tags = Set()
  val dataSourceType = DataSourceTypes.Greenplum

  // General
  val dataSourceParameter = Parameter.DataSource("data-source", dataSourceType, required = true)
  val generalGroup = ParameterGroup("general", List(dataSourceParameter))

  // Advanced
  val partitionColumnParameter = Parameter.String("partition-column")
  val partitionsParameter = Parameter.Long("partitions")

  val parameterGroups = List(ParameterGroup("general", List(dataSourceParameter)))

}