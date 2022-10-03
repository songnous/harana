package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow.actionobjects.PythonColumnTransformer
import com.harana.sdk.shared.models.flow.actiontypes.PythonColumnTransformationInfo

class PythonColumnTransformation extends TransformerAsActionType[PythonColumnTransformer] with PythonColumnTransformationInfo