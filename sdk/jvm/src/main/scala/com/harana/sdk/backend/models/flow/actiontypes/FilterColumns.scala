package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow.actionobjects.ColumnsFilterer
import com.harana.sdk.shared.models.flow.actiontypes.FilterColumnsInfo

class FilterColumns
  extends TransformerAsActionType[ColumnsFilterer]
  with FilterColumnsInfo