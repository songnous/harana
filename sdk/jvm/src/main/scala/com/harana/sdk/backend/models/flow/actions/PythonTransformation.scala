package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.flow.actionobjects.PythonTransformer
import com.harana.sdk.shared.models.flow.actions.PythonTransformationInfo

class PythonTransformation() extends TransformerAsAction[PythonTransformer]
  with PythonTransformationInfo