package com.harana.executor.spark.actiontypes.input

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup}
import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.shared.models.designer.data.DataSourceTypes.Sftp._
import com.harana.sdk.backend.models.flow.ActionType.{Inputs, Outputs}
import com.harana.sdk.backend.models.flow.actiontypes.input.GetSftpInfo
import com.harana.sdk.backend.models.flow.execution.ExecutionError
import com.harana.sdk.backend.models.flow.{ActionType, FlowContext}
import com.harana.sdk.backend.models.flow.actiontypes.pathParameter
import com.harana.executor.spark.actiontypes.dataSourceParameterValues
import zio.{IO, Task, UIO}

class GetSftp extends GetSftpInfo with ActionType {

  def validate(parameters: ParameterValues, context: FlowContext): UIO[List[ExecutionError]] = null

  def execute(inputs: Inputs, parameters: ParameterValues, context: FlowContext): IO[ExecutionError, Option[Outputs]] = {
    val spark = context.sparkSession
    val dsParameterValues = dataSourceParameterValues(context, parameters, dataSourceType, dataSourceParameter)

    val inferSchema = parameters(inferSchemaParameter)
    val header = parameters(headerParameter)
    val delimiter = parameters(delimiterParameter)
    val quote = parameters(quoteParameter)
    val escape = parameters(escapeParameter)
    val multiLine = parameters(multiLineParameter)

    val df = spark.read
      .format("com.springml.spark.sftp")
//      .option("host", dsParameterValues(hostParameter).value)
      .option("username", dsParameterValues(usernameParameter))
      .option("password", dsParameterValues(passwordParameter))
      .option("fileType", parameters(fileTypeParameter))
      .option("header", header.toString)
      .option("delimiter", delimiter)
      .option("quote", quote)
      .option("escape", escape)
      .option("multiLine", multiLine.toString)
      .option("inferSchema", inferSchema.toString)
      .load(parameters(pathParameter))

    IO.some(new Outputs(Map(outputPorts.head -> df)))
  }
}