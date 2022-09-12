package com.harana.designer.shared.flows.actiontypes.spark.output.next

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup, ParameterValue}
import com.harana.sdk.shared.models.designer.data.DataSourceTypes
import com.harana.designer.shared.flows.actiontypes.spark.output.OutputActionTypeInfo

class PutRedisInfo extends OutputActionTypeInfo {

  val tags = Set()

  // General
  val dataSourceType = DataSourceTypes.Redis
  val dataSourceParameter = Parameter.DataSource("data-source", dataSourceType, required = true)
  val tableParameter = Parameter.String("table", required = true)
  val generalGroup = ParameterGroup("general", List(dataSourceParameter, tableParameter))

  // Advanced
  val modelParameter = Parameter.String("model", options = List(
    ("binary", "binary"),
    ("enum", "enum"),
    ("hash", "hash")
  ))
  val filterKeysByTypeParameter = Parameter.Boolean("filterKeysByType")
  val keyColumnParameter = Parameter.String("keyColumn")
  val ttlParameter = Parameter.Long("ttl", default = Some(ParameterValue.Long(0)))
  val maxPipelineSizeParameter = Parameter.Long("maxPipelineSize", default = Some(ParameterValue.Long(100)))
  val advancedGroup = ParameterGroup("advanced", List(modelParameter, filterKeysByTypeParameter, keyColumnParameter, ttlParameter, maxPipelineSizeParameter))

  val parameterGroups = List(generalGroup, advancedGroup)

}
