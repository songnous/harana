package com.harana.executor.spark.actiontypes.input.next

import com.harana.sdk.backend.models.designer.flow.ActionType.{Inputs, Outputs}
import com.harana.sdk.backend.models.designer.flow.{ActionType, FlowContext}
import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.backend.models.designer.flow.actiontypes.input.next.GetCloudantInfo
import com.harana.sdk.backend.models.designer.flow.execution.ExecutionError
import com.harana.executor.spark.actiontypes.dataSourceParameterValues
import zio.{IO, UIO}

class GetCloudant extends GetCloudantInfo with ActionType {

  def validate(parameters: ParameterValues, context: FlowContext): UIO[List[ExecutionError]] = null

  def execute(inputs: Inputs, parameters: ParameterValues, context: FlowContext): IO[ExecutionError, Option[Outputs]] = {
    val spark = context.sparkSession
    val dsParameterValues = dataSourceParameterValues(context, parameters, dataSourceType, dataSourceParameter)

    val database = parameters(databaseParameter)
    val index = parameters(indexParameter)
    val view = parameters(viewParameter)
    val schemaSampleSize = parameters(schemaSampleSizeParameter)
    val selector = parameters(selectorParameter)
    val createDatabaseOnSave = parameters(createDatabaseOnSaveParameter)

    IO.none
  }
}