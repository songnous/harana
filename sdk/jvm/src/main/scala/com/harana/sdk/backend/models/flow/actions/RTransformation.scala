package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.flow.actionobjects.RTransformer
import com.harana.sdk.shared.models.flow.actions.RTransformationInfo
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation

class RTransformation extends TransformerAsAction[RTransformer] with RTransformationInfo