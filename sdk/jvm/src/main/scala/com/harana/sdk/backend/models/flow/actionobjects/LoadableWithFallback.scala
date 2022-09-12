package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.serialization.{CustomPersistence, SerializableSparkModel}
import org.apache.spark.ml

trait LoadableWithFallback[M <: ml.Model[M], E <: ml.Estimator[M]] { self: SparkModelWrapper[M, E] =>

  def tryToLoadModel(path: String): Option[M]

  def loadModel(ctx: ExecutionContext, path: String): SerializableSparkModel[M] = {
    tryToLoadModel(path) match {
      case Some(m) => new SerializableSparkModel(m)
      case None => CustomPersistence.load[SerializableSparkModel[M]](ctx.sparkContext, Transformer.modelFilePath(path))
    }
  }
}