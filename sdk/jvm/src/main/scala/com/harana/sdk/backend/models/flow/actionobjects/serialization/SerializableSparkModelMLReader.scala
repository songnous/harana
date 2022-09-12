package com.harana.sdk.backend.models.flow.actionobjects.serialization

import com.harana.sdk.backend.models.flow.actionobjects.Transformer
import com.harana.spark.ML
import com.harana.spark.ML.MLReaderWithSparkContext
import org.apache.spark.ml.util.MLReader

class SerializableSparkModelMLReader[M <: ML.Model[M]] extends MLReader[SerializableSparkModel[M]] with MLReaderWithSparkContext {

  def load(path: String) = {
    val modelPath = Transformer.modelFilePath(path)
    new SerializableSparkModel(CustomPersistence.load[M](sparkContext, modelPath))
  }
}
