package com.harana.executor.spark.actiontypes.output.next

import com.harana.sdk.backend.models.flow.ActionType.{Inputs, Outputs}
import com.harana.sdk.backend.models.flow.{Action, FlowContext}
import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.backend.models.flow.actiontypes.output.next.PutElasticsearchInfo
import com.harana.sdk.backend.models.flow.actiontypes.pathParameter
import com.harana.sdk.backend.models.flow.execution.ExecutionError
import com.harana.executor.spark.actiontypes.dataSourceParameterValues
import zio.{IO, UIO}

class PutElasticsearch extends PutElasticsearchInfo with Action {

  def validate(parameters: ParameterValues, context: FlowContext): UIO[List[ExecutionError]] = null

  def execute(inputs: Inputs, parameters: ParameterValues, context: FlowContext): IO[ExecutionError, Option[Outputs]] = {
    val spark = context.sparkSession
    val dsParameterValues = dataSourceParameterValues(context, parameters, dataSourceType, dataSourceParameter)
    val path = parameters(pathParameter)
    val writeAction = parameters(writeActionParameter)
    val mappingId = parameters(mappingIdParameter)
    val mappingExclude = parameters(mappingExcludeParameter)
    val mappingInclude = parameters(mappingIncludeParameter)
    val mappingJoin = parameters(mappingJoinParameter)
    val mappingParent = parameters(mappingParentParameter)
    val mappingRouting = parameters(mappingRoutingParameter)
    val mappingTimestamp = parameters(mappingTimestampParameter)
    val mappingVersion = parameters(mappingVersionParameter)
    val mappingVersionType = parameters(mappingVersionTypeParameter)

    IO.none
  }
}