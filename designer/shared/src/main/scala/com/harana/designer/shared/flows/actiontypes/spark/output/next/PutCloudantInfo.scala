package com.harana.designer.shared.flows.actiontypes.spark.output.next

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup}
import com.harana.designer.shared.flows.actiontypes.spark.ActionTypeGroup
import com.harana.sdk.shared.models.designer.data.DataSourceTypes
import com.harana.sdk.shared.models.designer.data.DataSourceTypes.Cloudant
import com.harana.designer.shared.flows.actiontypes.spark.output.OutputActionTypeInfo

class PutCloudantInfo extends OutputActionTypeInfo {

  val tags = Set()
  val dataSourceType = DataSourceTypes.Cloudant

  // General
  val dataSourceParameter = Parameter.DataSource("data-source", dataSourceType, required = true)
  val databaseParameter = Parameter.String("database", required = true)
  val indexParameter = Parameter.String("index", required = true)
  val viewParameter = Parameter.String("view")

  val generalGroup = ParameterGroup("general", List(
    dataSourceParameter, databaseParameter, indexParameter, viewParameter
  ))

  // Advanced
  val bulkSizeParameter = Cloudant.bulkSizeParameter
  val schemaSampleSizeParameter = Cloudant.schemaSampleSizeParameter
  val selectorParameter = Cloudant.selectorParameter
  val createDatabaseOnSaveParameter = Cloudant.createDatabaseOnSaveParameter

  val advancedGroup = ParameterGroup("advanced", List(
    bulkSizeParameter, schemaSampleSizeParameter, selectorParameter, createDatabaseOnSaveParameter
  ))

  val parameterGroups = List(generalGroup, advancedGroup)
}