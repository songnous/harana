package com.harana.designer.shared.flows.actiontypes.spark.output.next

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup}
import com.harana.designer.shared.flows.actiontypes.spark.ActionTypeGroup
import com.harana.sdk.shared.models.designer.data.DataSourceTypes
import com.harana.designer.shared.flows.actiontypes.spark.output.OutputActionTypeInfo

class PutMemSqlInfo extends OutputActionTypeInfo {

  val tags = Set()
  val dataSourceType = DataSourceTypes.MemSql

  // General
  val dataSourceParameter = Parameter.DataSource("data-source", dataSourceType, required = true)
  val databaseParameter = Parameter.String("database", required = true)
  val tableParameter = Parameter.String("table", required = true)
  val generalGroup = ParameterGroup("general", List(dataSourceParameter, databaseParameter, tableParameter))

  // Advanced
  val overwriteBehaviourParameter = Parameter.String("overwriteBehaviour", options = List(
    ("dropAndCreate", "dropAndCreate"),
    ("truncate", "truncate"),
    ("merge", "merge")
  ))
  val compressionParameter = Parameter.String("compression", options = List(
    ("lz4", "LZ4"),
    ("gzip", "GZip"),
    ("none", "None")
  ))
  val advancedGroup = ParameterGroup("advanced", List(overwriteBehaviourParameter, compressionParameter))

  val parameterGroups = List(generalGroup, advancedGroup)

}