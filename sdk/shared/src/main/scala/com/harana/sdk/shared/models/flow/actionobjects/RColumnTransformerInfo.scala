package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.parameters.{CodeSnippetLanguage, CodeSnippetParameter}

trait RColumnTransformerInfo extends CustomCodeColumnTransformerInfo {

  override val id = "AB01554B-4BBF-40E6-8729-7CBED40F2303"

  val codeParameter = CodeSnippetParameter("column action code", language = CodeSnippetLanguage(CodeSnippetLanguage.r))

  setDefault(codeParameter ->
      """transform.column <- function(column, column.name) {
        |  return(column)
        |}""".stripMargin
  )

  val specificParameters = Array(codeParameter, targetTypeParameter)

}

object RColumnTransformerInfo extends RColumnTransformerInfo