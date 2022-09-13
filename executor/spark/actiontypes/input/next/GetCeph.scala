package com.harana.executor.spark.actiontypes.input.next

import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.backend.models.flow.ActionType.{Inputs, Outputs}
import com.harana.sdk.backend.models.flow.execution.ExecutionError
import com.harana.sdk.backend.models.flow.{ActionType, FlowContext}
import com.harana.sdk.backend.models.flow.actiontypes.input.next.GetCephInfo
import com.harana.executor.spark.actiontypes.dataSourceParameterValues
import zio.{IO, UIO}

class GetCeph extends GetCephInfo with ActionType {

  def validate(parameters: ParameterValues, context: FlowContext): UIO[List[ExecutionError]] = null

  def execute(inputs: Inputs, parameters: ParameterValues, context: FlowContext): IO[ExecutionError, Option[Outputs]] = {
    val spark = context.sparkSession
    val dsParameterValues = dataSourceParameterValues(context, parameters, dataSourceType, dataSourceParameter)

    val host = dsParameterValues(dataSourceType.hostParameter)
    val ssl = dsParameterValues(dataSourceType.sslParameter)
    val accessKey = dsParameterValues(dataSourceType.accessKeyParameter)
    val secretKey = dsParameterValues(dataSourceType.secretKeyParameter)

    val database = parameters(databaseParameter)
    val index = parameters(indexParameter)
    val view = parameters(viewParameter)
    val bulkSize = parameters(bulkSizeParameter)
    val schemaSampleSize = parameters(schemaSampleSizeParameter)
    val selector = parameters(selectorParameter)
    val createDatabaseOnSave = parameters(createDatabaseOnSaveParameter)

    IO.none
  }
}