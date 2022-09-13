package com.harana.sdk.backend.models.flow.actions.spark.wrappers.transformers

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers.StringTokenizer
import com.harana.sdk.backend.models.flow.actions.TransformerAsAction
import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.transformers.TokenizeInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

class Tokenize extends TransformerAsAction[StringTokenizer]
  with TokenizeInfo
  with SparkActionDocumentation