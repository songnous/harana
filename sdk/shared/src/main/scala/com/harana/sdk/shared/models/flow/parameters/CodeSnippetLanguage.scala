package com.harana.sdk.shared.models.flow.parameters

import enumeratum.values.{StringCirceEnum, StringEnum, StringEnumEntry}

sealed abstract class CodeSnippetLanguage(val value: String) extends StringEnumEntry

case object CodeSnippetLanguage extends StringEnum[CodeSnippetLanguage] with StringCirceEnum[CodeSnippetLanguage] {
  case object Python extends CodeSnippetLanguage("python")
  case object R extends CodeSnippetLanguage("r")
  case object SQL extends CodeSnippetLanguage("sql")
  val values = findValues
}