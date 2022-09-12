package com.harana.executor.spark.actiontypes.transform

import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.shared.models.common.ParameterValue
import com.harana.sdk.backend.models.designer.flow.actiontypes.transform.AddColumnInfo
import com.harana.sdk.backend.models.designer.flow.execution.ExecutionError
import com.harana.sdk.backend.models.designer.flow.ActionType.{Inputs, Outputs}
import com.harana.sdk.backend.models.designer.flow.{ActionType, FlowContext}
import org.apache.spark.sql.DataFrame
import zio.{IO, Task, UIO}

class AddColumn extends AddColumnInfo with ActionType {

  def validate(parameters: ParameterValues, context: FlowContext): UIO[List[ExecutionError]] = null

  def execute(inputs: Inputs, parameters: ParameterValues, context: FlowContext): IO[ExecutionError, Option[Outputs]] =
    for {
      sc              <- UIO(context.sparkContext)
      inputDf         <- UIO(inputs(inputPorts.head))

      name            <- UIO(parameters(nameParameter))
      colType         <- UIO(parameters(typeParameter))
      colValue        <- UIO(parameters(valueParameter))
      colValueType    <- UIO(parameters(valueTypeParameter))

      outputs         <- IO.none
    } yield outputs
}