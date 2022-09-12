package com.harana.executor.spark.actiontypes.input

import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.shared.models.designer.data.DataSourceTypes.Oracle._
import com.harana.sdk.backend.models.designer.flow.ActionType.{Inputs, Outputs}
import com.harana.sdk.backend.models.designer.flow.actiontypes.input.GetOracleInfo
import com.harana.sdk.backend.models.designer.flow.execution.ExecutionError
import com.harana.sdk.backend.models.designer.flow.{ActionType, FlowContext}
import com.harana.executor.spark.actiontypes.{dataSourceParameterValues, log, setCommonJdbcOptions}
import zio.{IO, Task, UIO}

class GetOracle extends GetOracleInfo with ActionType {

  def validate(parameters: ParameterValues, context: FlowContext): UIO[List[ExecutionError]] = null

  def execute(inputs: Inputs, parameters: ParameterValues, context: FlowContext): IO[ExecutionError, Option[Outputs]] = {
    val spark = context.sparkSession
    val dsParameterValues = dataSourceParameterValues(context, parameters, dataSourceType, dataSourceParameter)

    val host = dsParameterValues(hostParameter)
    val database = parameters(databaseParameter)

    val reader = spark.read.format("jdbc").option("url", s"jdbc:oracle:thin:@$host:$database")
    setCommonJdbcOptions(reader, dsParameterValues, dataSourceType)
    setInputJdbcOptions(reader, parameters, this)

    val outputDf = reader.load()
    log(outputDf, parameters) *>
    IO.some(new Outputs(Map(outputPorts.head -> outputDf)))
  }
}