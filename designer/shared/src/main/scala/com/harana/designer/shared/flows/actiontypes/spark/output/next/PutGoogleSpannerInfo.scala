package com.harana.designer.shared.flows.actiontypes.spark.output.next

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup}
import com.harana.designer.shared.flows.actiontypes.spark.ActionTypeGroup
import com.harana.sdk.shared.models.designer.data.DataSourceTypes
import com.harana.designer.shared.flows.actiontypes.spark.output.OutputActionTypeInfo

class PutGoogleSpannerInfo extends OutputActionTypeInfo {

  val tags = Set()
  val dataSourceType = DataSourceTypes.GoogleSpanner
  val dataSourceParameter = Parameter.DataSource("data-source", dataSourceType, required = true)
  val parameterGroups = List(ParameterGroup("general", List(dataSourceParameter)))

}