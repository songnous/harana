package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.sdk.backend.models.flow.actionobjects.SparkTransformerAsMultiColumnTransformer
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers.BinarizerInfo
import org.apache.spark.ml.feature.{ Binarizer => SparkBinarizer }

class Binarizer extends SparkTransformerAsMultiColumnTransformer[SparkBinarizer] with BinarizerInfo