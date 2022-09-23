package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.parameters.{CodeSnippetLanguage, CodeSnippetParameter, StringParameter}

trait SqlTransformerInfo extends TransformerInfo {

  val id = "EFFE96C0-CE5A-4EC8-8B26-65349CF14462"

  val dataFrameIdParameter = StringParameter("dataframe id")
  setDefault(dataFrameIdParameter -> "df")
  def getDataFrameId = $(dataFrameIdParameter)
  def setDataFrameId(value: String): this.type = set(dataFrameIdParameter, value)

  val expressionParameter = CodeSnippetParameter("expression", language = CodeSnippetLanguage.SQL)
  setDefault(expressionParameter -> "SELECT * FROM df")
  def getExpression = $(expressionParameter)
  def setExpression(value: String): this.type = set(expressionParameter, value)

  val parameters = Left(Array(dataFrameIdParameter, expressionParameter))

}

object SqlTransformerInfo extends SqlTransformerInfo
