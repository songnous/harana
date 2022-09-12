package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.sdk.backend.models.flow.actionobjects.SparkTransformerAsMultiColumnTransformer
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers.RegexTokenizerInfo
import com.harana.sdk.shared.models.flow.parameters.{BooleanParameter, IntParameter, StringParameter}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import org.apache.spark.ml.feature.{RegexTokenizer => SparkRegexTokenizer}

class RegexTokenizer extends SparkTransformerAsMultiColumnTransformer[SparkRegexTokenizer] with RegexTokenizerInfo