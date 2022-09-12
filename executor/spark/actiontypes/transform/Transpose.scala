package com.harana.executor.spark.actiontypes.transform

import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.backend.models.designer.flow.ActionType.{Inputs, Outputs}
import com.harana.sdk.backend.models.designer.flow.actiontypes.transform.TransposeInfo
import com.harana.sdk.backend.models.designer.flow.execution.ExecutionError
import com.harana.sdk.backend.models.designer.flow.{ActionType, FlowContext}
import com.harana.executor.spark.actiontypes.log
import org.apache.spark.sql.functions.{col, collect_list, concat_ws}
import zio.{IO, Task, UIO}

class Transpose extends TransposeInfo with ActionType {

  def validate(parameters: ParameterValues, context: FlowContext): UIO[List[ExecutionError]] = null

  def execute(inputs: Inputs, parameters: ParameterValues, context: FlowContext): IO[ExecutionError, Option[Outputs]] = {
    val inputDf = inputs(inputPorts.head)
    val transposedColumns = parameters(transposedColumnsParameter)
    val pivotColumn = parameters(pivotColumnParameter)

    val columnsValue = transposedColumns.map(x => "'" + x + "', " + x)
    val stackCols = columnsValue.mkString(",")
    val df1 = inputDf
      .selectExpr(pivotColumn, "stack(" + transposedColumns.size + "," + stackCols + ")")
      .select(pivotColumn, "col0", "col1")

    val outputDf = df1.groupBy(col("col0"))
      .pivot(pivotColumn)
      .agg(concat_ws("", collect_list(col("col1"))))
      .withColumnRenamed("col0", pivotColumn)

    log(outputDf, parameters) *> IO.some(new Outputs(Map(outputPorts.head -> outputDf)))
  }
}