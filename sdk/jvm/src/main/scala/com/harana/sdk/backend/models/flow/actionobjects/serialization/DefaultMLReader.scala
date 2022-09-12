package com.harana.sdk.backend.models.flow.actionobjects.serialization

import com.harana.sdk.backend.models.flow.actionobjects.Transformer
import com.harana.spark.ML.MLReaderWithSparkContext
import org.apache.spark.ml.util.MLReader

class DefaultMLReader[T] extends MLReader[T] with MLReaderWithSparkContext {

  def load(path: String): T = {
    val modelPath = Transformer.modelFilePath(path)
    CustomPersistence.load(sparkContext, modelPath)
  }
}