package com.harana.executor.spark.actiontypes.input.next

import com.harana.sdk.backend.models.flow.ActionType.{Inputs, Outputs}
import com.harana.sdk.backend.models.flow.{ActionType, FlowContext}
import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.backend.models.flow.actiontypes.input.next.GetRedisInfo
import com.harana.sdk.backend.models.flow.execution.ExecutionError
import com.harana.executor.spark.actiontypes.dataSourceParameterValues
import zio.{IO, UIO}

class GetRedis extends GetRedisInfo with ActionType {

  def validate(parameters: ParameterValues, context: FlowContext): UIO[List[ExecutionError]] = null

  def execute(inputs: Inputs, parameters: ParameterValues, context: FlowContext): IO[ExecutionError, Option[Outputs]] = {
    val spark = context.sparkSession
    val dsParameterValues = dataSourceParameterValues(context, parameters, dataSourceType, dataSourceParameter)

    val table = parameters(tableParameter)
    val filterKeysByType = parameters(filterKeysByTypeParameter)
    val keyColumn = parameters(keyColumnParameter)
    val keysPattern = parameters(keysPatternParameter)
    val partitions = parameters(partitionsParameter)
    val inferSchema = parameters(inferSchemaParameter)
    val maxPipelineSize = parameters(maxPipelineSizeParameter)
    val scanCount = parameters(scanCountParameter)
    val iteratorGroupingSize = parameters(iteratorGroupingSizeParameter)
    
    IO.none
  }
}