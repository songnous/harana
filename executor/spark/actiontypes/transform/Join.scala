package com.harana.executor.spark.actiontypes.transform

import com.harana.sdk.shared.models.common.Parameter.{ParameterValues, parameter}
import com.harana.sdk.backend.models.flow.actiontypes.transform.JoinInfo
import com.harana.sdk.backend.models.flow.execution.ExecutionError
import com.harana.sdk.backend.models.flow.ActionType.{Inputs, Outputs}
import com.harana.sdk.backend.models.flow.{Action, FlowContext}
import com.harana.executor.spark.actiontypes.log
import zio.{IO, Task, UIO}

class Join extends JoinInfo with Action {

  def validate(parameters: ParameterValues, context: FlowContext): UIO[List[ExecutionError]] = null

  def execute(inputs: Inputs, parameters: ParameterValues, context: FlowContext): IO[ExecutionError, Option[Outputs]] = {
    val leftDf = inputs(inputPorts.head)
    val rightDf = inputs(inputPorts(1))
    val mode = parameters(modeParameter)
    val columns = parameters(columnsParameter)

    val outputDf = mode.value match {
      case "inner" => leftDf.join(rightDf, columns)
      case "left" => leftDf.join(rightDf, columns, "left_outer")
      case "right" => leftDf.join(rightDf, columns, "right_outer")
      case "outer" => leftDf.join(rightDf, columns, "outer")
    }

    log(outputDf, parameters) *> IO.some(new Outputs(Map(outputPorts.head -> outputDf)))
  }
}