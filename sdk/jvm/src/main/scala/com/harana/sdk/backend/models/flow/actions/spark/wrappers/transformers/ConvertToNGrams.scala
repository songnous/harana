package com.harana.sdk.backend.models.flow.actions.spark.wrappers.transformers

import com.harana.sdk.backend.models.designer.flow.actionobjects.spark.wrappers.transformers.NGramTransformer
import com.harana.sdk.backend.models.designer.flow.actions.TransformerAsAction
import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.transformers.ConvertToNGramsInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

class ConvertToNGrams extends TransformerAsAction[NGramTransformer]
  with ConvertToNGramsInfo
  with SparkActionDocumentation