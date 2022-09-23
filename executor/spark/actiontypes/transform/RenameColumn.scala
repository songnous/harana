package com.harana.executor.spark.actiontypes.transform

import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.shared.models.common.ParameterValue
import com.harana.sdk.backend.models.flow.actiontypes.transform.RenameColumnInfo
import com.harana.sdk.backend.models.flow.execution.ExecutionError
import com.harana.sdk.backend.models.flow.ActionType.{Inputs, Outputs}
import com.harana.sdk.backend.models.flow.{Action, FlowContext}
import com.harana.executor.spark.actiontypes.log
import org.apache.spark.sql.Column
import zio.{IO, Task, UIO}

class RenameColumn extends RenameColumnInfo with Action {

  def validate(parameters: ParameterValues, context: FlowContext): UIO[List[ExecutionError]] = null

  def execute(inputs: Inputs, parameters: ParameterValues, context: FlowContext): IO[ExecutionError, Option[Outputs]] = {
    val inputDf = inputs(inputPorts.head)

    val outputDf = inputDf.withColumnRenamed(parameters(existingNameParameter), parameters(newNameParameter) )

    log(outputDf, parameters) *> IO.some(new Outputs(Map(outputPorts.head -> outputDf)))
  }
}