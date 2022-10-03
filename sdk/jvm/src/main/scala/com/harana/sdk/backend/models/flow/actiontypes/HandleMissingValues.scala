package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow.actionobjects.MissingValuesHandler
import com.harana.sdk.shared.models.flow.actiontypes.HandleMissingValuesInfo

class HandleMissingValues extends TransformerAsActionType[MissingValuesHandler] with HandleMissingValuesInfo