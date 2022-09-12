package com.harana.designer.shared.flows.actiontypes.spark.input

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup}
import com.harana.sdk.shared.models.designer.data.DataSourceTypes
import com.harana.designer.shared.flows.actiontypes.spark._

class GetHaranaFilesInfo extends InputActionTypeInfo {

  val tags = Set("harana")
  val dataSourceType = DataSourceTypes.HaranaFiles
  val dataSourceParameter = Parameter.DataSource("data-source", dataSourceType, required = true)

  val parameterGroups = List(ParameterGroup("general", List(pathParameter, fileNameParameter, formatParameter)), logGroup)

}