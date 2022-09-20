package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.parameters.{CodeSnippetParameter, Parameter}
import com.harana.sdk.shared.models.flow.parameters.choice.ChoiceParameter

trait CustomCodeColumnTransformerInfo extends MultiColumnTransformerInfo {

  val targetTypeParameter = ChoiceParameter[TargetTypeChoice]("target type")
  def getTargetType = $(targetTypeParameter)
  def setTargetType(value: TargetTypeChoice): this.type = set(targetTypeParameter, value)

  val codeParameter: CodeSnippetParameter
  def getCodeParameter = $(codeParameter)
  def setCodeParameter(value: String): this.type = set(codeParameter, value)

  val parameters: Array[Parameter[_]]

}

object CustomCodeColumnTransformerInfo {
  val InputPortNumber: Int = 0
  val OutputPortNumber: Int = 0
}
