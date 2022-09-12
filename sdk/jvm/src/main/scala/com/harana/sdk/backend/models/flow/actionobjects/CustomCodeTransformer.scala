package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.ActionExecutionDispatcher.Result
import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actions.exceptions.CustomActionExecutionError
import com.harana.sdk.shared.models.flow.actionobjects.CustomCodeTransformerInfo
import com.harana.sdk.shared.models.flow.parameters.CodeSnippetParameter

abstract class CustomCodeTransformer extends Transformer with CustomCodeTransformerInfo {

  def isValid(context: ExecutionContext, code: String): Boolean

  def runCode(context: ExecutionContext, code: String): Result

  def applyTransform(ctx: ExecutionContext, df: DataFrame) = {
    if (!isValid(ctx, getCodeParameter)) throw CustomActionExecutionError("Code validation failed").toException

    ctx.dataFrameStorage.withInputDataFrame(InputPortNumber, df.sparkDataFrame) {
      runCode(ctx, getCodeParameter) match {
        case Left(error) => throw CustomActionExecutionError(s"Execution exception:\n\n$error").toException

        case Right(_) =>
          val sparkDataFrame = ctx.dataFrameStorage.getOutputDataFrame(OutputPortNumber).getOrElse {
            throw CustomActionExecutionError("action finished successfully, but did not produce a DataFrame.").toException
          }

          DataFrame.fromSparkDataFrame(sparkDataFrame)
      }
    }
  }
}