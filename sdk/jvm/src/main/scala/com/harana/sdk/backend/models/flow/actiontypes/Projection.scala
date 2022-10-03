package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow.actionobjects.Projector
import com.harana.sdk.shared.models.flow.actiontypes.ProjectionInfo

class Projection extends TransformerAsActionType[Projector] with ProjectionInfo