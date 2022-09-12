package com.harana.sdk.shared.models.flow.parameters

@SerialVersionUID(1)
case class CodeSnippetLanguage(language: CodeSnippetLanguage.CodeSnippetLanguage)

object CodeSnippetLanguage extends Enumeration {

  type CodeSnippetLanguage = Value

  val python = Value("python")
  val sql = Value("sql")
  val r = Value("r")

}