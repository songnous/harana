package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.parameters.{CodeSnippetLanguage, CodeSnippetParameter}

trait RTransformerInfo extends CustomCodeTransformerInfo {

  val id = "6E4BA79E-D953-4448-986A-5B620781912D"

  val default =
    """transform <- function(dataframe) {
      |  return(dataframe)
      |}
""".stripMargin

  override lazy val codeParameter = CodeSnippetParameter("code", default = Some(default), language = CodeSnippetLanguage.R)
}

object RTransformerInfo extends RTransformerInfo