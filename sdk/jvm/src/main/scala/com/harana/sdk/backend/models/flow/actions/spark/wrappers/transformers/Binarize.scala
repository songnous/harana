package com.harana.sdk.backend.models.flow.actions.spark.wrappers.transformers

import com.harana.sdk.backend.models.designer.flow.actionobjects.spark.wrappers.transformers.Binarizer
import com.harana.sdk.backend.models.designer.flow.actions.TransformerAsAction
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.transformers.BinarizeInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class Binarize extends TransformerAsAction[Binarizer]
  with BinarizeInfo
  with SparkActionDocumentation