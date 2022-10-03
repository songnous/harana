package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow.actionobjects.PythonTransformer
import com.harana.sdk.shared.models.flow.actiontypes.PythonTransformationInfo

class PythonTransformation() extends TransformerAsActionType[PythonTransformer]
  with PythonTransformationInfo