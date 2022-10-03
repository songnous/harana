package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.parameters.{CodeSnippetLanguage, CodeSnippetParameter}

trait PythonColumnTransformerInfo extends CustomCodeColumnTransformerInfo {

  override val id = "9C03A5DA-6E22-44D1-885B-204EA0B13A96"

  val codeParameter = CodeSnippetParameter("code",
    default = Some("def transform_value(value, column_name):\n    return value"),
    language = CodeSnippetLanguage.Python)

  val specificParameters = Array(codeParameter, targetTypeParameter)

}

object PythonColumnTransformerInfo extends PythonColumnTransformerInfo