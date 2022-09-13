package com.harana.executor.spark.actiontypes.output

import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.shared.models.designer.data.DataSourceTypes.MySql._
import com.harana.sdk.backend.models.flow.actiontypes.output.PutMySqlInfo
import com.harana.sdk.backend.models.flow.execution.ExecutionError
import com.harana.sdk.backend.models.flow.ActionType.{Inputs, Outputs}
import com.harana.sdk.backend.models.flow.{ActionType, FlowContext}
import com.harana.executor.spark.actiontypes.{dataSourceParameterValues, log}
import zio.{IO, Task, UIO}

class PutMySql extends PutMySqlInfo with ActionType {

  def validate(parameters: ParameterValues, context: FlowContext): UIO[List[ExecutionError]] = null

  def execute(inputs: Inputs, parameters: ParameterValues, context: FlowContext): IO[ExecutionError, Option[Outputs]] = {
    val dsParameterValues = dataSourceParameterValues(context, parameters, dataSourceType, dataSourceParameter)

    val inputDf = inputs(inputPorts.head)
    log(inputDf, parameters)

    inputDf
      .write
      .format("jdbc")
      .option("url", s"jdbc:mysql://${dsParameterValues(hostParameter)}/${parameters(databaseParameter)}")
      .option("dbtable", parameters(tableParameter))
      .option("user", dsParameterValues(usernameParameter))
      .option("password", dsParameterValues(passwordParameter))
      .option("isolationLevel", dsParameterValues(isolationLevelParameter))
      .option("numPartitions", dsParameterValues(numPartitionsParameter))
      .option("queryTimeout", dsParameterValues(queryTimeoutParameter))
      .option("sessionInitStatement", dsParameterValues(sessionInitStatementParameter))
      .option("batchSize", parameters(batchSizeParameter))
      .save()

    IO.none
  }
}