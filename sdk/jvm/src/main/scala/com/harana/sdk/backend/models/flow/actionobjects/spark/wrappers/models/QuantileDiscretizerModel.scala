package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.SparkSingleColumnParameterModelWrapper
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators.SparkSummaryEntry
import com.harana.sdk.backend.models.flow.actionobjects.serialization.SerializableSparkModel
import com.harana.sdk.shared.models.flow.actionobjects.report.Report
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.QuantileDiscretizerModelInfo
import com.harana.sdk.shared.models.flow.parameters.Parameter
import org.apache.spark.ml.feature.{Bucketizer => SparkQuantileDiscretizerModel, QuantileDiscretizer => SparkQuantileDiscretizer}

import scala.language.reflectiveCalls

class QuantileDiscretizerModel extends SparkSingleColumnParameterModelWrapper[SparkQuantileDiscretizerModel, SparkQuantileDiscretizer]
  with QuantileDiscretizerModelInfo {

  override def report(extended: Boolean = true) = {
    val summary = List(SparkSummaryEntry("splits", sparkModel.getSplits, "Split points for mapping continuous features into buckets."))
    super
      .report(extended)
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
  }

  def loadModel(ctx: ExecutionContext, path: String) = new SerializableSparkModel(SparkQuantileDiscretizerModel.load(path))
}