package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow.actionobjects.SqlColumnTransformer
import com.harana.sdk.shared.models.flow.actiontypes.SqlColumnTransformationInfo

class SqlColumnTransformation() extends TransformerAsActionType[SqlColumnTransformer] with SqlColumnTransformationInfo