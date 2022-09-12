package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.SparkModelWrapper
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators.SparkSummaryEntry
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators
import com.harana.sdk.backend.models.flow.actionobjects.serialization.SerializableSparkModel
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.ALSModelInfo
import org.apache.spark.ml.recommendation.{ALS => SparkALS, ALSModel => SparkALSModel}

class ALSModel extends SparkModelWrapper[SparkALSModel, SparkALS] with ALSModelInfo {

  override def report(extended: Boolean = true) = {
    val summary = List(SparkSummaryEntry("rank", sparkModel.rank, "Rank of the matrix factorization model."))
    super.report(extended).withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
  }

  def loadModel(ctx: ExecutionContext, path: String) = new SerializableSparkModel(SparkALSModel.load(path))

}