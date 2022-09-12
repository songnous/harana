package com.harana.executor.spark.actiontypes.output.next

import com.harana.sdk.backend.models.designer.flow.ActionType.{Inputs, Outputs}
import com.harana.sdk.backend.models.designer.flow.{ActionType, FlowContext}
import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.backend.models.designer.flow.actiontypes.output.next.PutAzureKustoInfo
import com.harana.sdk.backend.models.designer.flow.execution.ExecutionError
import zio.{IO, UIO}

class PutAzureKusto extends PutAzureKustoInfo with ActionType {

  def validate(parameters: ParameterValues, context: FlowContext): UIO[List[ExecutionError]] = null

  def execute(inputs: Inputs, parameters: ParameterValues, context: FlowContext): IO[ExecutionError, Option[Outputs]] = {
    val spark = context.sparkSession

    IO.none
  }
}