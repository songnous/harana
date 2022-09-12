package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.sdk.backend.models.flow.actionobjects.SparkTransformerAsMultiColumnTransformer
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers.DiscreteCosineTransformerInfo
import org.apache.spark.ml.feature.DCT

class DiscreteCosineTransformer extends SparkTransformerAsMultiColumnTransformer[DCT] with DiscreteCosineTransformerInfo {

  override def convertInputNumericToVector = true
  override def convertOutputVectorToDouble = true

}
