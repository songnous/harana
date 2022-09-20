package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.parameters.{CodeSnippetLanguage, CodeSnippetParameter}

trait REvaluatorInfo extends CustomCodeEvaluatorInfo {

  val id = "1331BA0B-4D5F-4856-A7D0-0ABFEE190702"

  val codeParameter = CodeSnippetParameter("R evaluator code", language = CodeSnippetLanguage(CodeSnippetLanguage.r))
  setDefault(
    codeParameter ->
      """evaluate <- function(dataframe){
        |    n <- nrow(dataframe)
        |    sq.error.column <- (dataframe$label - dataframe$prediction) ^ 2
        |    sq.error.sum.column <- sum(sq.error.column)
        |    sq.error.sum <- as.data.frame(agg(dataframe, sq.error.sum.column))
        |    rmse <- sqrt(sq.error.sum / n)
        |    return(rmse)
        |}""".stripMargin
  )

}

object REvaluatorInfo extends REvaluatorInfo