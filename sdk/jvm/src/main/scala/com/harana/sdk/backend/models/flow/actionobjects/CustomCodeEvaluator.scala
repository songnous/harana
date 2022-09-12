package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.ActionExecutionDispatcher.Result
import com.harana.sdk.backend.models.flow.{ExecutionContext, Knowledge}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actions.exceptions.CustomActionExecutionError
import com.harana.sdk.shared.models.flow.actionobjects.{CustomCodeEvaluatorInfo, MetricValue}
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.HasIsLargerBetterParameter
import com.harana.sdk.shared.models.flow.parameters.{CodeSnippetParameter, StringParameter}

abstract class CustomCodeEvaluator() extends Evaluator
  with CustomCodeEvaluatorInfo
  with HasIsLargerBetterParameter {

  def isValid(context: ExecutionContext, code: String): Boolean

  def runCode(context: ExecutionContext, code: String): Result
  def getComposedCode(userCode: String): String

  def _evaluate(ctx: ExecutionContext, df: DataFrame) = {
    val userCode = $(codeParameter)
    val composedCode = getComposedCode(userCode)

    if (!isValid(ctx, composedCode)) throw CustomActionExecutionError("Code validation failed").toException

    ctx.dataFrameStorage.withInputDataFrame(InputPortNumber, df.sparkDataFrame) {
      runCode(ctx, composedCode) match {
        case Left(error) =>
          throw CustomActionExecutionError(s"Execution exception:\n\n$error").toException

        case Right(_) =>
          val sparkDataFrame =
            ctx.dataFrameStorage.getOutputDataFrame(OutputPortNumber).getOrElse {
              throw CustomActionExecutionError("Function `evaluate` finished successfully, but did not produce a metric.").toException
            }

          val metricValue = sparkDataFrame.collect().head.getAs[Double](0)
          MetricValue(getMetricName, metricValue)
      }
    }
  }

  def _infer(k: Knowledge[DataFrame]): MetricValue = MetricValue.forInference(getMetricName)
}