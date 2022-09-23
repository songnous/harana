package com.harana.executor.spark.actiontypes.output.next

import com.harana.sdk.backend.models.flow.ActionType.{Inputs, Outputs}
import com.harana.sdk.backend.models.flow.{Action, FlowContext}
import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.backend.models.flow.actiontypes.output.next.PutBigQueryInfo
import com.harana.sdk.backend.models.flow.execution.ExecutionError
import com.harana.executor.spark.actiontypes.dataSourceParameterValues
import zio.{IO, UIO}

class PutBigQuery extends PutBigQueryInfo with Action {

  def validate(parameters: ParameterValues, context: FlowContext): UIO[List[ExecutionError]] = null

  def execute(inputs: Inputs, parameters: ParameterValues, context: FlowContext): IO[ExecutionError, Option[Outputs]] = {
    val spark = context.sparkSession
    val dsParameterValues = dataSourceParameterValues(context, parameters, dataSourceType, dataSourceParameter)
    
    val dataset = parameters(datasetParameter)
    val table = parameters(tableParameter)
    val project = parameters(projectParameter)
    val parentProject = parameters(parentProjectParameter)
    val maxParallelism = parameters(maxParallelismParameter)
    val viewsEnabled = parameters(viewsEnabledParameter)
    val materializationProject = parameters(materializationProjectParameter)
    val readDataFormat = parameters(readDataFormatParameter)
    val optimizedEmptyProjection = parameters(optimizedEmptyProjectionParameter)
    val createDisposition = parameters(createDispositionParameter)
    val persistentGcsBucket = parameters(persistentGcsBucketParameter)
    val persistentGcsPath = parameters(persistentGcsPathParameter)
    val intermediateFormat = parameters(intermediateFormatParameter)
    val datePartition = parameters(datePartitionParameter)
    val partitionField = parameters(partitionFieldParameter)
    val clusteredFields = parameters(clusteredFieldsParameter)
    val allowFieldAddition = parameters(allowFieldAdditionParameter)
    val allowFieldRelaxation = parameters(allowFieldRelaxationParameter)
    
    IO.none
  }
}