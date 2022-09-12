package com.harana.designer.shared.flows.actiontypes.spark.output

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup}
import com.harana.designer.shared.flows.actiontypes.spark._
import com.harana.sdk.shared.models.designer.data.DataSourceTypes

class PutSftpInfo extends OutputActionTypeInfo {

  val tags = Set()

  // General
  val dataSourceType = DataSourceTypes.Sftp
  val dataSourceParameter = Parameter.DataSource("data-source", dataSourceType, required = true)
  val fileTypeParameter = Parameter.String("file-type", options = List(
    ("avro", "avro"),
    ("csv", "csv"),
    ("json", "json"),
    ("parquet", "parquet"),
    ("text", "txt")
  ), required = true)
  val compressionParameter = Parameter.String("compression", options = List(
    ("none", "none"),
    ("bzip2", "bzip2"),
    ("gzip", "gzip"),
    ("lz4", "lz4"),
    ("snappy", "snappy"),
  ))

  val generalGroup = ParameterGroup("general", List(dataSourceParameter, pathParameter, fileNameParameter, fileTypeParameter, compressionParameter))
  val parameterGroups = List(generalGroup, logGroup)
}