package com.harana.designer.shared.flows.actiontypes.spark.input.next

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup}
import com.harana.sdk.shared.models.data.DataSourceType
import com.harana.sdk.shared.models.designer.data.{DataSourceTypes}

class GetWorkdayInfo extends InputActionTypeInfo {

  val tags = Set()

  // General
  val dataSourceType = DataSourceTypes.Workday
  val dataSourceParameter = Parameter.DataSource("data-source", dataSourceType, required = true)
  val requestParameter = Parameter.String("request")
  val objectTagPathParameter = Parameter.String("objectTagPath")
  val detailsTagPathParameter = Parameter.String("detailsTagPath")
  val xpathMapParameter = Parameter.String("xpathMap")
  val namespacePrefixMapParameter = Parameter.String("namespacePrefix")
  val generalGroup = ParameterGroup("general", List(dataSourceParameter, requestParameter, objectTagPathParameter, detailsTagPathParameter, xpathMapParameter, namespacePrefixMapParameter ))

  val parameterGroups = List(generalGroup)
}