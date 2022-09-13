package com.harana.executor.spark.actiontypes.input.next

import com.harana.sdk.backend.models.flow.ActionType.{Inputs, Outputs}
import com.harana.sdk.backend.models.flow.{ActionType, FlowContext}
import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.backend.models.flow.actiontypes.input.next.GetMarketoInfo
import com.harana.sdk.backend.models.flow.execution.ExecutionError
import com.harana.executor.spark.actiontypes.dataSourceParameterValues
import zio.{IO, UIO}

class GetMarketo extends GetMarketoInfo with ActionType {

  def validate(parameters: ParameterValues, context: FlowContext): UIO[List[ExecutionError]] = null

  def execute(inputs: Inputs, parameters: ParameterValues, context: FlowContext): IO[ExecutionError, Option[Outputs]] = {
    val spark = context.sparkSession
    val dsParameterValues = dataSourceParameterValues(context, parameters, dataSourceType, dataSourceParameter)

    val `object` = parameters(objectParameter)
    val filterKey = parameters(filterKeyParameter)
    val filterValues = parameters(filterValuesParameter)
    val sinceDateTime = parameters(sinceDateTimeParameter)

    IO.none
  }
}