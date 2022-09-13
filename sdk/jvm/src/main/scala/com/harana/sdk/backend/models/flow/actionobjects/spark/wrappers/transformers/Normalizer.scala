package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.sdk.backend.models.flow.actionobjects.SparkTransformerAsMultiColumnTransformer
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers.NormalizerInfo
import org.apache.spark.ml.feature.{Normalizer => SparkNormalizer}

class Normalizer extends SparkTransformerAsMultiColumnTransformer[SparkNormalizer] with NormalizerInfo {

  override def convertInputNumericToVector: Boolean = true
  override def convertOutputVectorToDouble: Boolean = true

}