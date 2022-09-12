package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.SparkSingleColumnParameterModelWrapper
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators.SparkSummaryEntry
import com.harana.sdk.backend.models.flow.actionobjects.serialization.SerializableSparkModel
import com.harana.sdk.shared.models.flow.actionobjects.report.Report
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.StandardScalerModelInfo
import com.harana.sdk.shared.models.flow.parameters.Parameter
import org.apache.spark.ml.feature.{StandardScaler => SparkStandardScaler, StandardScalerModel => SparkStandardScalerModel}

class StandardScalerModel extends SparkSingleColumnParameterModelWrapper[SparkStandardScalerModel, SparkStandardScaler]
  with StandardScalerModelInfo {

  override def convertInputNumericToVector = true
  override def convertOutputVectorToDouble = true

  override def report(extended: Boolean = true) = {
    val summary = List(
      SparkSummaryEntry("std", sparkModel.std, "Vector of standard deviations of the model."),
      SparkSummaryEntry("mean", sparkModel.mean, "Vector of means of the model.")
    )

    super
      .report(extended)
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
  }

  def loadModel(ctx: ExecutionContext, path: String) = new SerializableSparkModel(SparkStandardScalerModel.load(path))

}