package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.flow.actionobjects.RowsFilterer
import com.harana.sdk.shared.models.flow.actions.FilterRowsInfo

class FilterRows
  extends TransformerAsAction[RowsFilterer]
  with FilterRowsInfo