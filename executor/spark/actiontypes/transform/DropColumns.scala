package com.harana.executor.spark.actiontypes.transform

import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.shared.models.common.ParameterValue
import com.harana.sdk.backend.models.flow.actiontypes.transform.DropColumnsInfo
import com.harana.sdk.backend.models.flow.execution.ExecutionError
import com.harana.sdk.backend.models.flow.ActionType.{Inputs, Outputs}
import com.harana.sdk.backend.models.flow.{ActionType, FlowContext}
import com.harana.executor.spark.actiontypes.log
import org.apache.spark.sql.DataFrame
import zio.{IO, Task, UIO}

class DropColumns extends DropColumnsInfo with ActionType {

  def validate(parameters: ParameterValues, context: FlowContext): UIO[List[ExecutionError]] = null

  def execute(inputs: Inputs, parameters: ParameterValues, context: FlowContext): IO[ExecutionError, Option[Outputs]] =
    for {
      inputDf   <- UIO(inputs(inputPorts.head))
      columns   <- UIO(parameters(columnsParameter))
      outputDf  <- UIO(inputDf.drop(columns: _*))
      _         <- log(outputDf, parameters)
      outputs   <- IO.some(new Outputs(Map(outputPorts.head -> outputDf)))
    } yield outputs
}