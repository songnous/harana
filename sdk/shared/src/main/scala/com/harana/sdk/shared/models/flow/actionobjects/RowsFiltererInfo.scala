package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.parameters.{CodeSnippetLanguage, CodeSnippetParameter}

trait RowsFiltererInfo extends TransformerInfo {

  val id = "719C9C08-09CA-490B-97F4-CFD2368C7A0E"

  val conditionParameter = CodeSnippetParameter("condition", language = CodeSnippetLanguage.SQL)
  def getCondition = $(conditionParameter)
  def setCondition(value: String): this.type = set(conditionParameter, value)
  val parameters = Left(Array(conditionParameter))

}

object RowsFiltererInfo extends RowsFiltererInfo