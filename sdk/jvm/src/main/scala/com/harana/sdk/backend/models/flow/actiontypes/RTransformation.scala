package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow.actionobjects.RTransformer
import com.harana.sdk.shared.models.flow.actiontypes.RTransformationInfo
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation

class RTransformation extends TransformerAsActionType[RTransformer] with RTransformationInfo