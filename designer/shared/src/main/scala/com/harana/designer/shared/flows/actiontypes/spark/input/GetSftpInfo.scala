package com.harana.designer.shared.flows.actiontypes.spark.input

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup, ParameterValue}
import com.harana.sdk.shared.models.designer.data.DataSourceTypes
import com.harana.designer.shared.flows.actiontypes.spark._

class GetSftpInfo extends InputActionTypeInfo {

  val tags = Set()
  val dataSourceType = DataSourceTypes.Sftp

  val dataSourceParameter = Parameter.DataSource("data-source", dataSourceType, required = true)
  val fileTypeParameter = Parameter.String("file-type", options = List(
    ("avro", "avro"),
    ("csv", "csv"),
    ("json", "json"),
    ("parquet", "parquet"),
    ("text", "txt")
  ), required = true)
  val generalGroup = ParameterGroup("general", List(dataSourceParameter, fileTypeParameter, pathParameter, fileNameParameter))

  val inferSchemaParameter = Parameter.Boolean("infer-schema")
  val headerParameter = Parameter.Boolean("header")
  val delimiterParameter = Parameter.String("delimiter", Some(ParameterValue.String(",")))
  val quoteParameter = Parameter.String("quote", Some(ParameterValue.String("\"")))
  val escapeParameter = Parameter.String("escape", Some(ParameterValue.String("\\")))
  val multiLineParameter = Parameter.Boolean("multi-line")
  val csvGroup = ParameterGroup("csv", List(inferSchemaParameter, headerParameter, delimiterParameter, quoteParameter, escapeParameter, multiLineParameter))

  val parameterGroups = List(generalGroup, logGroup)
}