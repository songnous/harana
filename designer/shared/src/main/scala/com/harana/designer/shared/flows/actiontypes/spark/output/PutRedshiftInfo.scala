package com.harana.designer.shared.flows.actiontypes.spark.output

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup}
import com.harana.designer.shared.flows.actiontypes.spark.{ActionTypeGroup, logGroup}
import com.harana.sdk.shared.models.designer.data.DataSourceTypes

class PutRedshiftInfo extends JdbcOutputActionTypeInfo {

  val tags = Set()
  val dataSourceType = DataSourceTypes.Redshift
  val dataSourceParameter = Parameter.DataSource("data-source", dataSourceType, required = true)
  val generalGroup = ParameterGroup("general", List(dataSourceParameter) ++ jdbcGeneralParameters)
  val parameterGroups = List(generalGroup, jdbcAdvancedGroup, logGroup)

}