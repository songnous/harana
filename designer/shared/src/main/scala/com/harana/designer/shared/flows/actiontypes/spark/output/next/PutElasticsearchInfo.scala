package com.harana.designer.shared.flows.actiontypes.spark.output.next

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup}
import com.harana.sdk.shared.models.designer.data.DataSourceTypes
import com.harana.designer.shared.flows.actiontypes.spark._
import com.harana.designer.shared.flows.actiontypes.spark.output.OutputActionTypeInfo

class PutElasticsearchInfo extends OutputActionTypeInfo {

  val tags = Set()
  val dataSourceType = DataSourceTypes.Elasticsearch

  // General
  val dataSourceParameter = Parameter.DataSource("data-source", dataSourceType, required = true)
  val generalGroup = ParameterGroup("general", List(dataSourceParameter, pathParameter))

  val writeActionParameter = Parameter.String("write-action", required = true, options =
    List(
      ("index", "index"),
      ("create", "create"),
      ("update", "update"),
      ("upsert", "upsert")
    ))

  val mappingIdParameter = Parameter.String("mapping-id")
  val mappingExcludeParameter = Parameter.String("mapping-exclude")
  val mappingIncludeParameter = Parameter.String("mapping-include")
  val mappingJoinParameter = Parameter.String("mapping-join")
  val mappingParentParameter = Parameter.String("mapping-parent")
  val mappingRoutingParameter = Parameter.String("mapping-routing")
  val mappingTimestampParameter = Parameter.String("mapping-timestamp")
  val mappingVersionParameter = Parameter.String("mapping-version")
  val mappingVersionTypeParameter = Parameter.String("mapping-version-type")

  val mappingGroup = ParameterGroup("mapping", List(
    mappingIdParameter, mappingExcludeParameter, mappingIncludeParameter, mappingJoinParameter, mappingParentParameter,
    mappingRoutingParameter, mappingTimestampParameter, mappingVersionParameter, mappingVersionTypeParameter))

  val parameterGroups = List(generalGroup, mappingGroup, logGroup)

}
