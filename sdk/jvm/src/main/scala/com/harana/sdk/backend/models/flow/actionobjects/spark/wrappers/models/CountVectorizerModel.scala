package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.SparkSingleColumnParameterModelWrapper
import com.harana.sdk.backend.models.flow.actionobjects.serialization.SerializableSparkModel
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.CountVectorizerModelInfo
import org.apache.spark.ml.feature.{CountVectorizer => SparkCountVectorizer, CountVectorizerModel => SparkCountVectorizerModel}

class CountVectorizerModel
    extends SparkSingleColumnParameterModelWrapper[SparkCountVectorizerModel, SparkCountVectorizer]
    with CountVectorizerModelInfo {

  def loadModel(ctx: ExecutionContext, path: String) = new SerializableSparkModel(SparkCountVectorizerModel.load(path))
}
