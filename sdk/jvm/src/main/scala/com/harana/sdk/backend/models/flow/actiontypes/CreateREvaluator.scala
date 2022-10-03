package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow.actionobjects.REvaluator
import com.harana.sdk.shared.models.flow.actiontypes.CreateREvaluatorInfo

class CreateREvaluator extends EvaluatorAsFactory[REvaluator]
  with CreateREvaluatorInfo


//-> EvaluatorAsFactory -> Action0To1 -> Action0To1Info
//-> CreateREvaluatorInfo -> EvaluatorAsFactoryInfo -> Action0To1Info -> ActionInfo