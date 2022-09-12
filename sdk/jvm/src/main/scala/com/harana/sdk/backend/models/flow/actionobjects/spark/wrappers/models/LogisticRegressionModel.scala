package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.SparkModelWrapper
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators.SparkSummaryEntry
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators
import com.harana.sdk.backend.models.flow.actionobjects.serialization.SerializableSparkModel
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.LogisticRegressionModelInfo
import org.apache.spark.ml.classification.{LogisticRegressionTrainingSummary, LogisticRegression => SparkLogisticRegression, LogisticRegressionModel => SparkLogisticRegressionModel}

class LogisticRegressionModel
    extends SparkModelWrapper[SparkLogisticRegressionModel, SparkLogisticRegression]
    with LogisticRegressionModelInfo {

  override def report(extended: Boolean = true) = {
    val coefficients = SparkSummaryEntry("coefficients", sparkModel.coefficients, "Weights computed for every feature.")

    val summary = if (sparkModel.hasSummary) {
      val modelSummary: LogisticRegressionTrainingSummary = sparkModel.summary
      List(
        SparkSummaryEntry("objective history", modelSummary.objectiveHistory, "Objective function (scaled loss + regularization) at each iteration."),
        SparkSummaryEntry("total iterations", modelSummary.totalIterations, "Number of training iterations until termination.")
      )
    } else List()

    super.report(extended).withAdditionalTable(CommonTablesGenerators.modelSummary(List(coefficients) ++ summary))
  }

  def loadModel(ctx: ExecutionContext, path: String) = new SerializableSparkModel(SparkLogisticRegressionModel.load(path))
}
