package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.SparkSingleColumnParameterModelWrapper
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators.SparkSummaryEntry
import com.harana.sdk.backend.models.flow.actionobjects.serialization.SerializableSparkModel
import com.harana.sdk.shared.models.flow.actionobjects.report.Report
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.IDFModelInfo
import com.harana.sdk.shared.models.flow.parameters.Parameter
import org.apache.spark.ml.feature.{IDF => SparkIDF, IDFModel => SparkIDFModel}

class IDFModel extends SparkSingleColumnParameterModelWrapper[SparkIDFModel, SparkIDF]
  with IDFModelInfo {

  override def report(extended: Boolean = true) = {
    val summary = List(SparkSummaryEntry("IDF vector", sparkModel.idf, "The inverse document frequency vector."))
    super
      .report(extended)
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
  }

  def loadModel(ctx: ExecutionContext, path: String) = new SerializableSparkModel(SparkIDFModel.load(path))
}
