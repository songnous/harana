package com.harana.designer.shared.flows.actiontypes.spark.output

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup}
import com.harana.designer.shared.flows.actiontypes.spark.{ActionTypeGroup, logGroup}
import com.harana.sdk.shared.models.designer.data.DataSourceTypes

class PutSnowflakeInfo extends OutputActionTypeInfo {

  val tags = Set()
  val dataSourceType = DataSourceTypes.Snowflake

  // General
  val dataSourceParameter = Parameter.DataSource("data-source", dataSourceType, required = true)
  val databaseParameter = Parameter.String("database", required = true)
  val tableParameter = Parameter.String("table", required = true)
  val generalGroup = ParameterGroup("general", List(dataSourceParameter, databaseParameter, tableParameter, saveModeParameter))

  val parameterGroups = List(generalGroup, logGroup)
}