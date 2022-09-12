package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.sdk.backend.models.flow.actionobjects.SparkTransformerAsMultiColumnTransformer
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers.HashingTFTransformerInfo
import org.apache.spark.ml.feature.HashingTF

class HashingTFTransformer extends SparkTransformerAsMultiColumnTransformer[HashingTF] with HashingTFTransformerInfo
