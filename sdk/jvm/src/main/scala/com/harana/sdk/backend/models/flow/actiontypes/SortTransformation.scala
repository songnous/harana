package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow.actionobjects.SortTransformer
import com.harana.sdk.shared.models.flow.actiontypes.SortTransformationInfo

class SortTransformation
  extends TransformerAsActionType[SortTransformer]
  with SortTransformationInfo