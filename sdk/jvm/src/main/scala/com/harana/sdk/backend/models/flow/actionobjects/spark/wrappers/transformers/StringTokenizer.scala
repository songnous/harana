package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.sdk.backend.models.flow.actionobjects.SparkTransformerAsMultiColumnTransformer
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers.StringTokenizerInfo
import com.harana.sdk.shared.models.flow.parameters.Parameter
import org.apache.spark.ml.feature.Tokenizer

class StringTokenizer extends SparkTransformerAsMultiColumnTransformer[Tokenizer] with StringTokenizerInfo {
  override val specificParameters = Array.empty[Parameter[_]]
}