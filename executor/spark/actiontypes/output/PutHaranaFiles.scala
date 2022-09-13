package com.harana.executor.spark.actiontypes.output

import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.shared.models.designer.data.DataSourceTypes.S3._
import com.harana.sdk.backend.models.flow.ActionType.{Inputs, Outputs}
import com.harana.sdk.backend.models.flow.actiontypes.output.PutHaranaFilesInfo
import com.harana.sdk.backend.models.flow.actiontypes.{formatParameter, pathParameter}
import com.harana.sdk.backend.models.flow.execution.ExecutionError
import com.harana.sdk.backend.models.flow.{ActionType, FlowContext}
import com.harana.executor.spark.actiontypes.{dataSourceParameterValues, log, param, writeFile}
import com.harana.executor.spark.utils.PathUtils
import zio.{IO, Task, UIO}

class PutHaranaFiles extends PutHaranaFilesInfo with ActionType {

  def validate(parameters: ParameterValues, context: FlowContext): UIO[List[ExecutionError]] = null

  def execute(inputs: Inputs, values: ParameterValues, context: FlowContext): IO[ExecutionError, Option[Outputs]] =
    for {
      spark                 <- UIO(context.sparkSession)
      path                  <- param(values, pathParameter).map(_.get)
      userPath              =  s"file://${context.haranaFilesPath}/${PathUtils.userPath(context.userId, Some(path))}"
      format                <- param(values, formatParameter)
      inputDf               =  inputs.get(inputPorts.head)

      _                     <- inputDf match {
                                case Some(df) => writeFile(spark, df, format.get, userPath).mapError(ExecutionError.Unknown)
                                case None => IO.unit
                              }

      _                     <- IO.when(inputDf.isDefined)(log(inputDf.get, values))
      output                <- IO.none
    } yield output
}