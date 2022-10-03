package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow.actionobjects.RowsFilterer
import com.harana.sdk.shared.models.flow.actiontypes.FilterRowsInfo

class FilterRows
  extends TransformerAsActionType[RowsFilterer]
  with FilterRowsInfo