package com.harana.executor.spark.actiontypes.transform

import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.backend.models.flow.ActionType.{Inputs, Outputs}
import com.harana.sdk.backend.models.flow.actiontypes.transform.{MergeInfo, RepartitionInfo}
import com.harana.sdk.backend.models.flow.execution.ExecutionError
import com.harana.sdk.backend.models.flow.{Action, FlowContext}
import com.harana.executor.spark.actiontypes.log
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row
import zio.{IO, Task, UIO}

class Repartition extends RepartitionInfo with Action {

  def validate(parameters: ParameterValues, context: FlowContext): UIO[List[ExecutionError]] = null

  def execute(inputs: Inputs, parameters: ParameterValues, context: FlowContext): IO[ExecutionError, Option[Outputs]] = {
    val inputDf = inputs(inputPorts.head)
    val partitionCount = parameters(partitionCountParameter)

    val outputDf =
      if (partitionCount < inputDf.rdd.getNumPartitions)
        inputDf.coalesce(partitionCount).toDF()
      else
        inputDf.sqlContext.createDataFrame(inputDf.rdd.repartition(partitionCount), inputDf.schema)

    log(outputDf, parameters) *> IO.some(new Outputs(Map(outputPorts.head -> outputDf)))
  }
}