package com.harana.designer.shared.flows.actiontypes.spark.input.next

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup}
import com.harana.sdk.shared.models.designer.data.DataSourceTypes

class GetCephInfo extends InputActionTypeInfo {

  val tags = Set()
  val dataSourceType = DataSourceTypes.Ceph
  val dataSourceParameter = Parameter.DataSource("data-source", dataSourceType, required = true)

  // General
  val databaseParameter = Parameter.String("database", required = true)
  val indexParameter = Parameter.String("index", required = true)
  val viewParameter = Parameter.String("view")
  val generalGroup = ParameterGroup("general", List(databaseParameter, indexParameter, viewParameter))

  // Advanced
  val bulkSizeParameter = Parameter.Long("bulkSize", placeholder = Some(200))
  val schemaSampleSizeParameter = Parameter.Long("schemaSampleSize", placeholder = Some(-1))
  val selectorParameter = Parameter.String("selector")
  val createDatabaseOnSaveParameter = Parameter.Boolean("createDatabaseOnSave")
  val advancedGroup = ParameterGroup("advanced", List(bulkSizeParameter, schemaSampleSizeParameter, selectorParameter, createDatabaseOnSaveParameter))

  val parameterGroups = List(generalGroup, advancedGroup)
}