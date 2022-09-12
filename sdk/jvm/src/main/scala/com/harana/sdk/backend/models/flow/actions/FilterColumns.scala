package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.flow.actionobjects.ColumnsFilterer
import com.harana.sdk.shared.models.flow.actions.FilterColumnsInfo

class FilterColumns
  extends TransformerAsAction[ColumnsFilterer]
  with FilterColumnsInfo