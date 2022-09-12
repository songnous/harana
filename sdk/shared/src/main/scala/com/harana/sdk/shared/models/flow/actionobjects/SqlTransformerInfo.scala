package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.parameters.{CodeSnippetLanguage, CodeSnippetParameter, StringParameter}

trait SqlTransformerInfo extends TransformerInfo {

  val id = "EFFE96C0-CE5A-4EC8-8B26-65349CF14462"

  val dataFrameIdParameter = StringParameter("dataframe id", Some("An identifier that can be used in the SQL expression to refer to the input DataFrame."))
  setDefault(dataFrameIdParameter -> "df")
  def getDataFrameId = $(dataFrameIdParameter)
  def setDataFrameId(value: String): this.type = set(dataFrameIdParameter, value)

  val expressionParameter = CodeSnippetParameter("expression", Some("SQL Expression to be executed on the DataFrame."), language = CodeSnippetLanguage(CodeSnippetLanguage.sql))
  setDefault(expressionParameter -> "SELECT * FROM df")
  def getExpression = $(expressionParameter)
  def setExpression(value: String): this.type = set(expressionParameter, value)

  val parameters = Array(dataFrameIdParameter, expressionParameter)

}

object SqlTransformerInfo extends SqlTransformerInfo
