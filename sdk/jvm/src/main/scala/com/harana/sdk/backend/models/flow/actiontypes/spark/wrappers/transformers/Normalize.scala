package com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.transformers

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers.Normalizer
import com.harana.sdk.backend.models.flow.actiontypes.TransformerAsActionType
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers.Normalizer
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.transformers.NormalizeInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class Normalize extends TransformerAsActionType[Normalizer]
  with NormalizeInfo
  with SparkActionDocumentation