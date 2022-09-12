package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.designer.flow._
import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.shared.models.flow.actionobjects.PythonEvaluatorInfo

class PythonEvaluator extends CustomCodeEvaluator with PythonEvaluatorInfo {

  def runCode(context: ExecutionContext, code: String) = context.customCodeExecutor.runPython(code)

  def isValid(context: ExecutionContext, code: String) = context.customCodeExecutor.isPythonValid(code)

}