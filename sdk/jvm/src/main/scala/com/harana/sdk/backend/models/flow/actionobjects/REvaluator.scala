package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.ActionExecutionDispatcher._
import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.shared.models.flow.actionobjects.REvaluatorInfo
import com.harana.sdk.shared.models.flow.parameters.{CodeSnippetLanguage, CodeSnippetParameter}

class REvaluator extends CustomCodeEvaluator with REvaluatorInfo {

  def runCode(context: ExecutionContext, code: String) =
    context.customCodeExecutor.runR(code)

  def isValid(context: ExecutionContext, code: String): Boolean =
    context.customCodeExecutor.isRValid(code)

  // Creating a dataframe is a workaround. Currently we can pass to jvm DataFrames only.
  // TODO DS-3695 Fix a metric value - dataframe workaround.
  def getComposedCode(userCode: String) = {
    s"""
       |$userCode
       |
       |transform <- function(dataframe) {
       |    result <- evaluate(dataframe)
       |    numeric.result <- as.numeric(result)
       |    if (is.na(numeric.result)) {
       |      stop("Invalid result of evaluate function: value '",
       |       result, "' cannot be converted to float.")
       |    }
       |
       |    result.df <- createDataFrame(as.data.frame(numeric.result))
       |    return(result.df)
       |}
      """.stripMargin
  }
}
