package com.harana.designer.shared.flows.actiontypes.spark.input.next

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup}
import com.harana.sdk.shared.models.designer.data.DataSourceTypes

class GetBigQueryInfo extends InputActionTypeInfo {

  val tags = Set("google", "gcp")
  val dataSourceType = DataSourceTypes.BigQuery

  // General
  val dataSourceParameter = Parameter.DataSource("data-source", dataSourceType, required = true)
  val datasetParameter = Parameter.String("dataset", required = true)
  val tableParameter = Parameter.String("table", required = true)
  val projectParameter = Parameter.String("project")

  // Advanced
  val parentProjectParameter = Parameter.String("parentProject")
  val maxParallelismParameter = Parameter.Long("maxParallelism")
  val viewsEnabledParameter = Parameter.Boolean("viewsEnabled")
  val materializationProjectParameter = Parameter.String("materializationProject")
  val readDataFormatParameter = Parameter.String("readDataFormat", options = List(
    ("arrow", "arrow"),
    ("avro", "avro")
  ))
  val optimizedEmptyProjectionParameter = Parameter.Boolean("optimizedEmptyProjection")

  // Groups
  val generalGroup = ParameterGroup("general", List(
    dataSourceParameter, datasetParameter, tableParameter, projectParameter
  ))

  val advancedGroup = ParameterGroup("advanced", List(
    parentProjectParameter, maxParallelismParameter, viewsEnabledParameter, materializationProjectParameter,
    readDataFormatParameter, optimizedEmptyProjectionParameter
  ))

  val parameterGroups = List(generalGroup, advancedGroup)

}