package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.SparkModelWrapper
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators.SparkSummaryEntry
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators
import com.harana.sdk.backend.models.flow.actionobjects.serialization.SerializableSparkModel
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.UnivariateFeatureModelInfo
import org.apache.spark.ml.feature.{UnivariateFeatureSelector => SparkUnivariateFeatureSelector, UnivariateFeatureSelectorModel => SparkUnivariateFeatureSelectorModel}

class UnivariateFeatureSelectorModel
    extends SparkModelWrapper[SparkUnivariateFeatureSelectorModel, SparkUnivariateFeatureSelector]
    with UnivariateFeatureModelInfo {

  override def report(extended: Boolean = true)= {
    val summary = List(SparkSummaryEntry("selected features", sparkModel.selectedFeatures, "List of indices to select."))
    super.report(extended).withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
  }

  def loadModel(ctx: ExecutionContext, path: String) = new SerializableSparkModel(SparkUnivariateFeatureSelectorModel.load(path))
}
