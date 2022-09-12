package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.SparkSingleColumnParameterModelWrapper
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators
import com.harana.sdk.backend.models.flow.actionobjects.serialization.SerializableSparkModel
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.VectorIndexerModelInfo
import org.apache.spark.ml.feature.{VectorIndexer => SparkVectorIndexer, VectorIndexerModel => SparkVectorIndexerModel}

class VectorIndexerModel extends SparkSingleColumnParameterModelWrapper[SparkVectorIndexerModel, SparkVectorIndexer]
  with VectorIndexerModelInfo {

  override def report(extended: Boolean = true) =
    super
      .report(extended)
      .withAdditionalTable(CommonTablesGenerators.categoryMaps(sparkModel.categoryMaps))

  def loadModel(ctx: ExecutionContext, path: String) =
    new SerializableSparkModel(SparkVectorIndexerModel.load(path))
}