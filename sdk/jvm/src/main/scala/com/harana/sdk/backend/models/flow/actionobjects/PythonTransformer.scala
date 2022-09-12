package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.shared.models.flow.actionobjects.PythonTransformerInfo
import com.harana.sdk.shared.models.flow.parameters.{CodeSnippetLanguage, CodeSnippetParameter}

class PythonTransformer extends CustomCodeTransformer with PythonTransformerInfo {

  def isValid(context: ExecutionContext, code: String) = context.customCodeExecutor.isPythonValid(code)

  def runCode(context: ExecutionContext, code: String) = context.customCodeExecutor.runPython(code)

}
