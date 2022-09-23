  package com.harana.executor.spark.actiontypes.input

import com.harana.sdk.shared.models.common.Parameter.ParameterValues
  import com.harana.sdk.shared.models.designer.data.DataSourceTypes.MySql._
  import com.harana.sdk.backend.models.flow.ActionType.{Inputs, Outputs}
  import com.harana.sdk.backend.models.flow.actiontypes.input.GetMySqlInfo
  import com.harana.sdk.backend.models.flow.execution.ExecutionError
  import com.harana.sdk.backend.models.flow.{Action, FlowContext}
  import com.harana.executor.spark.actiontypes.{dataSourceParameterValues, log, setCommonJdbcOptions}
  import com.mongodb.spark.MongoSpark
  import zio.{IO, Task, UIO}

  class GetMySql extends GetMySqlInfo with Action {

    def validate(parameters: ParameterValues, context: FlowContext): UIO[List[ExecutionError]] = null

    def execute(inputs: Inputs, parameters: ParameterValues, context: FlowContext): IO[ExecutionError, Option[Outputs]] = {
      val spark = context.sparkSession
      val dsParameterValues = dataSourceParameterValues(context, parameters, dataSourceType, dataSourceParameter)

      val host = dsParameterValues(hostParameter)
      val database = parameters(databaseParameter)

      val reader = spark.read.format("jdbc").option("url", s"jdbc:mysql://$host/$database")
      setCommonJdbcOptions(reader, dsParameterValues, dataSourceType)
      setInputJdbcOptions(reader, parameters, this)

      val outputDf = reader.load()
      log(outputDf, parameters) *>
      IO.some(new Outputs(Map(outputPorts.head -> outputDf)))
    }
  }