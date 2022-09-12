package com.harana.designer.shared.flows.actiontypes.spark.output.next

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup}
import com.harana.designer.shared.flows.actiontypes.spark.ActionTypeGroup
import com.harana.sdk.shared.models.designer.data.DataSourceTypes
import com.harana.designer.shared.flows.actiontypes.spark.output.OutputActionTypeInfo

class PutBigQueryInfo extends OutputActionTypeInfo {

  val tags = Set()
  val dataSourceType = DataSourceTypes.BigQuery

  val dataSourceParameter = Parameter.DataSource("data-source", dataSourceType, required = true)
  val datasetParameter = Parameter.String("dataset", required = true)
  val tableParameter = Parameter.String("table", required = true)
  val projectParameter = Parameter.String("project")

  val parentProjectParameter = Parameter.String("parentProject")
  val maxParallelismParameter = Parameter.Long("maxParallelism")
  val viewsEnabledParameter = Parameter.Boolean("viewsEnabled")
  val materializationProjectParameter = Parameter.String("materializationProject")
  val readDataFormatParameter = Parameter.String("readDataFormat", options = List(
    ("arrow", "arrow"),
    ("avro", "avro")
  ))
  val optimizedEmptyProjectionParameter = Parameter.Boolean("optimizedEmptyProjection")
  val createDispositionParameter = Parameter.String("createDisposition", options = List(
    ("createIfNeeded", "createIfNeeded"),
    ("createNever", "createNever")
  ))
  val persistentGcsBucketParameter = Parameter.String("persistentGcsBucket")
  val persistentGcsPathParameter = Parameter.String("persistentGcsPath")
  val intermediateFormatParameter = Parameter.String("intermediateFormat", options = List(
    ("parquet", "parquet"),
    ("orc", "orc"),
    ("avro", "avro")
  ))
  val datePartitionParameter = Parameter.String("datePartition")
  val partitionFieldParameter = Parameter.String("partitionField")
  val clusteredFieldsParameter = Parameter.StringList("clusteredFields")
  val allowFieldAdditionParameter = Parameter.Boolean("allowFieldAddition")
  val allowFieldRelaxationParameter = Parameter.Boolean("allowFieldRelaxation")

  val defaultGroup = ParameterGroup("general", List(
    dataSourceParameter, datasetParameter, tableParameter, projectParameter
  ))

  val advancedGroup = ParameterGroup("advanced", List(
    parentProjectParameter, maxParallelismParameter, viewsEnabledParameter, materializationProjectParameter, readDataFormatParameter, optimizedEmptyProjectionParameter, createDispositionParameter, persistentGcsBucketParameter, persistentGcsPathParameter, intermediateFormatParameter, datePartitionParameter, partitionFieldParameter, clusteredFieldsParameter, allowFieldAdditionParameter, allowFieldRelaxationParameter
  ))

  val parameterGroups = List(defaultGroup, advancedGroup)
}