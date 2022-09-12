package com.harana.designer.shared.flows.actiontypes.spark.input.next

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup}
import com.harana.sdk.shared.models.designer.data.DataSourceTypes
import com.harana.designer.shared.flows.actiontypes.spark._

class GetElasticsearchInfo extends InputActionTypeInfo {

  val tags = Set("search")
  val dataSourceType = DataSourceTypes.Elasticsearch

  // General
  val dataSourceParameter = Parameter.DataSource("data-source", dataSourceType, required = true)
  val queryParameter = Parameter.String("query", multiLine = true, placeholder = Some("?q=Smith"))
  val generalGroup = ParameterGroup("general", List(dataSourceParameter, pathParameter, queryParameter))

  // Advanced
  val includeMetadataParameter = Parameter.Boolean("include-metadata")
  val includeMetadataVersionParameter = Parameter.Boolean("include-metadata-version")
  val metadataFieldParameter = Parameter.Boolean("metadata-field")
  val pushdownParameter = Parameter.Boolean("pushdown")
  val fieldsToIncludeParameter = Parameter.StringList("fields-to-include")
  val fieldsToExcludeParameter = Parameter.StringList("fields-to-exclude")
  val fieldsToIncludeAsArrayParameter = Parameter.StringList("fields-to-include-as-array")
  val fieldsToExcludeAsArrayParameter = Parameter.StringList("fields-to-exclude-as-array")
  val advancedGroup = ParameterGroup("advanced", List(includeMetadataParameter, includeMetadataVersionParameter, metadataFieldParameter, pushdownParameter,
    fieldsToIncludeParameter, fieldsToExcludeParameter, fieldsToIncludeAsArrayParameter, fieldsToExcludeAsArrayParameter))

  val parameterGroups = List(generalGroup, advancedGroup, logGroup)

}