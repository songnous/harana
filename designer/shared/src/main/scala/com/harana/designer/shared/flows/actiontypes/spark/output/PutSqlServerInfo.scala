package com.harana.designer.shared.flows.actiontypes.spark.output

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup}
import com.harana.sdk.shared.models.designer.data.DataSourceTypes
import com.harana.designer.shared.flows.actiontypes.spark.logGroup

class PutSqlServerInfo extends JdbcOutputActionTypeInfo {

  val tags = Set()
  val dataSourceType = DataSourceTypes.SqlServer
  val dataSourceParameter = Parameter.DataSource("data-source", dataSourceType, required = true)
  val generalGroup = ParameterGroup("general", List(dataSourceParameter) ++ jdbcGeneralParameters)
  val parameterGroups = List(generalGroup, jdbcAdvancedGroup, logGroup)

}