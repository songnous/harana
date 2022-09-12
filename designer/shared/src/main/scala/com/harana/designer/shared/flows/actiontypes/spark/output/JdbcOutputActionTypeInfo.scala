package com.harana.designer.shared.flows.actiontypes.spark.output

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup, ParameterValue}

abstract class JdbcOutputActionTypeInfo extends OutputActionTypeInfo {

  val databaseParameter = Parameter.String("database")
  val tableParameter = Parameter.String("table")
  val jdbcGeneralParameters = List(databaseParameter, tableParameter)

  val batchSizeParameter = Parameter.Long("batch-size", Some(ParameterValue.Long(1000)))
  val jdbcAdvancedGroup = ParameterGroup("advanced", List(saveModeParameter, batchSizeParameter))
}