package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.flow.actionobjects.PythonEvaluator
import com.harana.sdk.shared.models.flow.actions.CreatePythonEvaluatorInfo

class CreatePythonEvaluator extends EvaluatorAsFactory[PythonEvaluator]
  with CreatePythonEvaluatorInfo