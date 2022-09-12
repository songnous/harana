package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.SparkSingleColumnParameterModelWrapper
import com.harana.sdk.backend.models.flow.actionobjects.serialization.SerializableSparkModel
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.Word2VecModelInfo
import org.apache.spark.ml.feature.{Word2Vec => SparkWord2Vec, Word2VecModel => SparkWord2VecModel}

class Word2VecModel extends SparkSingleColumnParameterModelWrapper[SparkWord2VecModel, SparkWord2Vec]
  with Word2VecModelInfo {

  def loadModel(ctx: ExecutionContext, path: String) = new SerializableSparkModel(SparkWord2VecModel.load(path))

}