package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.SparkModelWrapper
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators.SparkSummaryEntry
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators
import com.harana.sdk.backend.models.flow.actionobjects.serialization.SerializableSparkModel
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.IsotonicRegressionModelInfo
import org.apache.spark.ml.regression.{IsotonicRegression => SparkIsotonicRegression, IsotonicRegressionModel => SparkIsotonicRegressionModel}

class IsotonicRegressionModel
    extends SparkModelWrapper[SparkIsotonicRegressionModel, SparkIsotonicRegression]
    with IsotonicRegressionModelInfo {

  override def report(extended: Boolean = true) = {
    val summary = List(
      SparkSummaryEntry("boundaries", sparkModel.boundaries, "Boundaries in increasing order for which predictions are known."),
      SparkSummaryEntry("predictions", sparkModel.predictions, "Predictions associated with the boundaries at the same index, monotone because of isotonic regression.")
    )
    super
      .report(extended)
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
  }

  def loadModel(ctx: ExecutionContext, path: String) =
    new SerializableSparkModel(SparkIsotonicRegressionModel.load(path))
}
