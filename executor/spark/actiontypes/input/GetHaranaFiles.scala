package com.harana.executor.spark.actiontypes.input

import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.shared.models.designer.data.DataSourceTypes.S3._
import com.harana.sdk.backend.models.flow.ActionType.{Inputs, Outputs}
import com.harana.sdk.backend.models.flow.actiontypes.input.{GetHaranaFilesInfo, GetS3Info}
import com.harana.sdk.backend.models.flow.actiontypes.{fileNameParameter, formatParameter, pathParameter}
import com.harana.sdk.backend.models.flow.execution.ExecutionError
import com.harana.sdk.backend.models.flow.{Action, FlowContext}
import com.harana.executor.spark.actiontypes.{dataSourceParameterValues, log}
import com.harana.executor.spark.utils.PathUtils
import zio.{IO, Task, UIO}

class GetHaranaFiles extends GetHaranaFilesInfo with Action {

  def validate(parameters: ParameterValues, context: FlowContext): UIO[List[ExecutionError]] = null

  def execute(inputs: Inputs, parameters: ParameterValues, context: FlowContext): IO[ExecutionError, Option[Outputs]] = {
    val spark = context.sparkSession

    val path = parameters(pathParameter)
    val fileName = parameters(fileNameParameter)
    val format = parameters(formatParameter)

    val df = readFile(spark, format, s"file://${PathUtils.userPath(context.userId, Some(path), Some(fileName))}").mapError(e => ExecutionError.Unknown(e))
    IO.some(new Outputs(Map(outputPorts.head -> df)))
  }
}