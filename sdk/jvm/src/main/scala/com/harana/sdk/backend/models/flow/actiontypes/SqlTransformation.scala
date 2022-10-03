package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow.actionobjects.SqlTransformer
import com.harana.sdk.shared.models.flow.actiontypes.SqlTransformationInfo

class SqlTransformation extends TransformerAsActionType[SqlTransformer] with SqlTransformationInfo