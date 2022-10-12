package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.parameters.{CodeSnippetParameter, ParameterGroup}

trait CustomCodeTransformerInfo extends TransformerInfo {

  val InputPortNumber: Int = 0
  val OutputPortNumber: Int = 0

  val codeParameter: CodeSnippetParameter
  def getCodeParameter = $(codeParameter)
  def setCodeParameter(value: String): this.type = set(codeParameter, value)
  override val parameterGroups = List(ParameterGroup("", codeParameter))

}