package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.flow.actionobjects.MissingValuesHandler
import com.harana.sdk.shared.models.flow.actions.HandleMissingValuesInfo

class HandleMissingValues extends TransformerAsAction[MissingValuesHandler] with HandleMissingValuesInfo