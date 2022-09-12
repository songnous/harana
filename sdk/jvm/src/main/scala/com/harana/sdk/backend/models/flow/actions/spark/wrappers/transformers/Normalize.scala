package com.harana.sdk.backend.models.flow.actions.spark.wrappers.transformers

import com.harana.sdk.backend.models.designer.flow.actionobjects.spark.wrappers.transformers.Normalizer
import com.harana.sdk.backend.models.designer.flow.actions.TransformerAsAction
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers.Normalizer
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.transformers.NormalizeInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class Normalize extends TransformerAsAction[Normalizer]
  with NormalizeInfo
  with SparkActionDocumentation