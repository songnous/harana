package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.shared.models.flow.actionobjects.RTransformerInfo
import com.harana.sdk.shared.models.flow.parameters.{CodeSnippetLanguage, CodeSnippetParameter}

class RTransformer extends CustomCodeTransformer with RTransformerInfo {

  def isValid(context: ExecutionContext, code: String) = context.customCodeExecutor.isRValid(code)
  def runCode(context: ExecutionContext, code: String) = context.customCodeExecutor.runR(code)

}