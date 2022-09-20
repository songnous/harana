package com.harana.sdk.backend.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.{CodeSnippetLanguage, CodeSnippetParameter}
import io.circe.Json
import io.circe.syntax.EncoderOps

class CodeSnippetParameterSpec extends AbstractParameterSpec[String, CodeSnippetParameter] {

  def className = "CodeSnippetParameter"

  def paramFixture: (CodeSnippetParameter, Json) = {
      val param       = CodeSnippetParameter(
      "myName",
      Some(description),
      CodeSnippetLanguage(CodeSnippetLanguage.python)
    )
    val js          = Map(
                        "type"        -> Json.fromString("codeSnippet"),
                        "name"        -> Json.fromString(param.name),
                        "description" -> Json.fromString(description),
                        "language"    -> Map("name" -> Json.fromString("python")).asJson,
                        "isGriddable" -> Json.False,
                        "default"     -> Json.Null
                      )
    (param, js.asJson)
  }

  def valueFixture: (String, Json) = ("some python code", "some python code".asJson)

}