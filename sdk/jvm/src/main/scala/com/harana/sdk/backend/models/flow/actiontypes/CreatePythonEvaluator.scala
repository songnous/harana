package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow.actionobjects.PythonEvaluator
import com.harana.sdk.shared.models.flow.actiontypes.CreatePythonEvaluatorInfo

class CreatePythonEvaluator extends EvaluatorAsFactory[PythonEvaluator]
  with CreatePythonEvaluatorInfo