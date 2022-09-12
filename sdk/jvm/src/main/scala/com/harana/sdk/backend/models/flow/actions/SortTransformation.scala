package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.flow.actionobjects.SortTransformer
import com.harana.sdk.shared.models.flow.actions.SortTransformationInfo
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation

class SortTransformation
  extends TransformerAsAction[SortTransformer]
  with SortTransformationInfo