package com.harana.sdk.shared.models.flow.parameters

case class CodeSnippetParameter(name: String,
                                required: Boolean = false,
                                language: CodeSnippetLanguage) extends Parameter[String] {

  val parameterType: ParameterType = ParameterType.CodeSnippet

  override def replicate(name: String) = copy(name = name)

}