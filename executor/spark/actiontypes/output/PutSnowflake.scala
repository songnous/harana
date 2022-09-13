package com.harana.executor.spark.actiontypes.output

import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.shared.models.common.{Parameter, ParameterValue}
import com.harana.sdk.backend.models.flow.actiontypes.output.PutSnowflakeInfo
import com.harana.sdk.backend.models.flow.execution.ExecutionError
import com.harana.sdk.backend.models.flow.ActionType.{Inputs, Outputs}
import com.harana.sdk.backend.models.flow.{ActionType, FlowContext}
import com.harana.executor.spark.actiontypes.{dataSourceParameterValues, log}
import zio.{IO, Task, UIO}

class PutSnowflake extends PutSnowflakeInfo with ActionType {

  def validate(parameters: ParameterValues, context: FlowContext): UIO[List[ExecutionError]] = null

  def execute(inputs: Inputs, parameters: ParameterValues, context: FlowContext): IO[ExecutionError, Option[Outputs]] = {
    val spark = context.sparkSession
    val dsParameterValues = dataSourceParameterValues(context, parameters, dataSourceType, dataSourceParameter)

    val inputDf = inputs(inputPorts.head)
    log(inputDf, parameters)

    val database = parameters(databaseParameter)
    val table = parameters(tableParameter)

    IO.none
  }
}