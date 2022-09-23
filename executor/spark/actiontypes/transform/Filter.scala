package com.harana.executor.spark.actiontypes.transform

import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.shared.models.common.ParameterValue
import com.harana.sdk.backend.models.flow.actiontypes.transform.FilterInfo
import com.harana.sdk.backend.models.flow.execution.ExecutionError
import com.harana.sdk.backend.models.flow.ActionType.{Inputs, Outputs}
import com.harana.sdk.backend.models.flow.{Action, FlowContext}
import com.harana.executor.spark.actiontypes.log
import zio.{IO, Task, UIO}

class Filter extends FilterInfo with Action {

  def validate(parameters: ParameterValues, context: FlowContext): UIO[List[ExecutionError]] = null

  def execute(inputs: Inputs, parameters: ParameterValues, context: FlowContext): IO[ExecutionError, Option[Outputs]] = {
    val inputDf = inputs(inputPorts.head)
    val condition = parameters(conditionParameter)

    val outputDf = inputDf.filter(condition)

    log(outputDf, parameters) *> IO.some(new Outputs(Map(outputPorts.head -> outputDf)))
  }
}