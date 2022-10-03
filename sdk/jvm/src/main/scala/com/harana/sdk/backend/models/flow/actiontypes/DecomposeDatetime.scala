package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow.actionobjects.DatetimeDecomposer
import com.harana.sdk.shared.models.flow.actiontypes.DecomposeDatetimeInfo

class DecomposeDatetime extends TransformerAsActionType[DatetimeDecomposer] with DecomposeDatetimeInfo