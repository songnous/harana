package com.harana.executor.spark.actiontypes.input.next

import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.backend.models.flow.ActionType.{Inputs, Outputs}
import com.harana.sdk.backend.models.flow.execution.ExecutionError
import com.harana.sdk.backend.models.flow.{Action, FlowContext}
import com.harana.sdk.backend.models.flow.actiontypes.input.next.GetMemSqlInfo
import zio.{IO, UIO}

class GetMemSql extends GetMemSqlInfo with Action {

  def validate(parameters: ParameterValues, context: FlowContext): UIO[List[ExecutionError]] = null

  def execute(inputs: Inputs, parameters: ParameterValues, context: FlowContext): IO[ExecutionError, Option[Outputs]] = {
    val spark = context.sparkSession

    val database = parameters(databaseParameter)
    val table = parameters(tableParameter)

    IO.none
  }
}