package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.sdk.backend.models.flow.actionobjects.SparkTransformerAsMultiColumnTransformer
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers.NGramTransformerInfo
import org.apache.spark.ml.feature.NGram

class NGramTransformer extends SparkTransformerAsMultiColumnTransformer[NGram] with NGramTransformerInfo