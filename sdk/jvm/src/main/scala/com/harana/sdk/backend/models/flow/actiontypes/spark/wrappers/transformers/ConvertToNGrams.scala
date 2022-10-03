package com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.transformers

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers.NGramTransformer
import com.harana.sdk.backend.models.flow.actiontypes.TransformerAsActionType
import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.transformers.ConvertToNGramsInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

class ConvertToNGrams extends TransformerAsActionType[NGramTransformer]
  with ConvertToNGramsInfo
  with SparkActionDocumentation