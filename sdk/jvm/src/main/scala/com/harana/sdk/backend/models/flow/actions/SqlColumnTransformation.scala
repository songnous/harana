package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.flow.actionobjects.SqlColumnTransformer
import com.harana.sdk.shared.models.flow.actions.SqlColumnTransformationInfo

class SqlColumnTransformation() extends TransformerAsAction[SqlColumnTransformer] with SqlColumnTransformationInfo