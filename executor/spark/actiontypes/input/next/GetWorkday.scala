package com.harana.executor.spark.actiontypes.input.next

import com.harana.sdk.backend.models.flow.ActionType.{Inputs, Outputs}
import com.harana.sdk.backend.models.flow.{Action, FlowContext}
import com.harana.sdk.shared.models.common.Parameter
import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.backend.models.flow.actiontypes.input.next.GetWorkdayInfo
import com.harana.sdk.backend.models.flow.execution.ExecutionError
import com.harana.executor.spark.actiontypes.dataSourceParameterValues
import zio.{IO, UIO}

class GetWorkday extends GetWorkdayInfo with Action {

  def validate(parameters: ParameterValues, context: FlowContext): UIO[List[ExecutionError]] = null

  def execute(inputs: Inputs, parameters: ParameterValues, context: FlowContext): IO[ExecutionError, Option[Outputs]] = {
    val spark = context.sparkSession
    val dsParameterValues = dataSourceParameterValues(context, parameters, dataSourceType, dataSourceParameter)

    val request = parameters(requestParameter)
    val objectTagPath = parameters(objectTagPathParameter)
    val detailsTagPath = parameters(detailsTagPathParameter)
    val xpathMap = parameters(xpathMapParameter)
    val namespacePrefixMap = Parameter.String("namespacePrefix")

    IO.none
  }
}