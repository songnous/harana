package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.flow.actionobjects.SortTransformer
import com.harana.sdk.shared.models.flow.actions.SortTransformationInfo

class SortTransformation
  extends TransformerAsAction[SortTransformer]
  with SortTransformationInfo