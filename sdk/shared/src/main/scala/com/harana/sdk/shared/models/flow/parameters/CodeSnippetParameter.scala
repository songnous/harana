package com.harana.sdk.shared.models.flow.parameters

case class CodeSnippetParameter(name: String,
                                description: Option[String],
                                language: CodeSnippetLanguage) extends Parameter[String] {

  val parameterType: ParameterType = ParameterType.CodeSnippet

  override def replicate(name: String) = copy(name = name)

}