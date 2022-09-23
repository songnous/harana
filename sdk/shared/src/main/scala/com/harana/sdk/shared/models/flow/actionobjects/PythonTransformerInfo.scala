package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.parameters.{CodeSnippetLanguage, CodeSnippetParameter}

trait PythonTransformerInfo extends CustomCodeTransformerInfo {

  val id = "EEAEBAE4-52C5-4FF7-899D-ABFF0EB2CFC6"

  override lazy val codeParameter = CodeSnippetParameter("code", language = CodeSnippetLanguage.Python)
  setDefault(codeParameter -> "def transform(dataframe):\n    return dataframe")

}

object PythonTransformerInfo extends PythonTransformerInfo