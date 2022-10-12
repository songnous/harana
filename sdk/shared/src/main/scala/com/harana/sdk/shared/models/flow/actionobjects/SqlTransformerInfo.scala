package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.parameters.{CodeSnippetLanguage, CodeSnippetParameter, ParameterGroup, StringParameter}

trait SqlTransformerInfo extends TransformerInfo {

  val id = "EFFE96C0-CE5A-4EC8-8B26-65349CF14462"

  val dataFrameIdParameter = StringParameter("dataframe-id", default = Some("df"))
  def getDataFrameId = $(dataFrameIdParameter)
  def setDataFrameId(value: String): this.type = set(dataFrameIdParameter, value)

  val expressionParameter = CodeSnippetParameter("expression", default = Some("SELECT * FROM df"), language = CodeSnippetLanguage.SQL)
  def getExpression = $(expressionParameter)
  def setExpression(value: String): this.type = set(expressionParameter, value)

  override val parameterGroups = List(ParameterGroup("", dataFrameIdParameter, expressionParameter))

}

object SqlTransformerInfo extends SqlTransformerInfo
