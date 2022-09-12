package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.SparkSingleColumnParameterModelWrapper
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators.SparkSummaryEntry
import com.harana.sdk.backend.models.flow.actionobjects.serialization.SerializableSparkModel
import com.harana.sdk.shared.models.flow.actionobjects.report.Report
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.MinMaxScalerModelInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.MinMaxParameters
import com.harana.sdk.shared.models.flow.parameters.Parameter
import org.apache.spark.ml.feature.{MinMaxScaler => SparkMinMaxScaler, MinMaxScalerModel => SparkMinMaxScalerModel}

class MinMaxScalerModel
    extends SparkSingleColumnParameterModelWrapper[SparkMinMaxScalerModel, SparkMinMaxScaler]
    with MinMaxScalerModelInfo {

  override def convertInputNumericToVector = true
  override def convertOutputVectorToDouble = true

  override def report(extended: Boolean = true) = {
    val summary = List(
      SparkSummaryEntry("original min", sparkModel.originalMin, "Minimal value for each original column during fitting."),
      SparkSummaryEntry("original max", sparkModel.originalMax, "Maximum value for each original column during fitting.")
    )

    super
      .report(extended)
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
  }

  def loadModel(ctx: ExecutionContext, path: String) = new SerializableSparkModel(SparkMinMaxScalerModel.load(path))
}
