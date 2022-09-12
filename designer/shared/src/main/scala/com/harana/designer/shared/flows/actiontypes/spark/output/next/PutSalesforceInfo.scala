package com.harana.designer.shared.flows.actiontypes.spark.output.next

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup}
import com.harana.designer.shared.flows.actiontypes.spark.ActionTypeGroup
import com.harana.sdk.shared.models.designer.data.DataSourceTypes
import com.harana.designer.shared.flows.actiontypes.spark.output.OutputActionTypeInfo

class PutSalesforceInfo extends OutputActionTypeInfo {

  val tags = Set()

  // General
  val dataSourceType = DataSourceTypes.Salesforce
  val dataSourceParameter = Parameter.DataSource("data-source", dataSourceType, required = true)
  val modeParameter = Parameter.String("mode", options = List(
    ("createDataset", "createDataset"),
    ("updateObject", "updateObject")
  ), required = true)
  val datasetParameter = Parameter.String("dataset")
  val objectParameter = Parameter.String("object")
  val generalGroup = ParameterGroup("general", List(dataSourceParameter, datasetParameter, objectParameter))

  val parameterGroups = List(generalGroup)

}