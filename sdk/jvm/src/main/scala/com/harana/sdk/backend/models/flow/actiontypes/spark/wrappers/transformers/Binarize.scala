package com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.transformers

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers.Binarizer
import com.harana.sdk.backend.models.flow.actiontypes.TransformerAsActionType
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.transformers.BinarizeInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class Binarize extends TransformerAsActionType[Binarizer]
  with BinarizeInfo
  with SparkActionDocumentation