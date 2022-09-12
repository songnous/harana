package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.flow.actionobjects.DatetimeDecomposer
import com.harana.sdk.shared.models.flow.actions.DecomposeDatetimeInfo

class DecomposeDatetime extends TransformerAsAction[DatetimeDecomposer] with DecomposeDatetimeInfo