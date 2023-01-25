package com.harana.executor.spark.actiontypes.query

import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.shared.models.common.ParameterValue
import com.harana.sdk.backend.models.flow.actiontypes.query.ExecuteSQLInfo
import com.harana.sdk.backend.models.flow.actiontypes.transform.ForkInfo
import com.harana.sdk.backend.models.flow.execution.ExecutionError
import com.harana.sdk.backend.models.flow.ActionType.{Inputs, Outputs}
import com.harana.sdk.backend.models.flow.{ActionType, FlowContext}
import com.harana.executor.spark.actiontypes.log
import zio.{IO, Task, UIO}

class ExecuteSQL extends ExecuteSQLInfo with ActionType {

  def validate(parameters: ParameterValues, context: FlowContext): UIO[List[ExecutionError]] = null

  def execute(inputs: Inputs, parameters: ParameterValues, context: FlowContext): IO[ExecutionError, Option[Outputs]] = {
    val query = parameters(queryParameter)
    val viewName = parameters(viewNameParameter)

    val inputDf = inputs(inputPorts.head)
    log(inputDf, parameters)

    inputDf.createTempView(viewName)

    val outputDf = context.sparkSession.sql(query)
    log(outputDf, parameters) *>
    IO.some(new Outputs(Map(outputPorts.head -> outputDf)))
  }
}