package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.SparkModelWrapper
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators.SparkSummaryEntry
import com.harana.sdk.backend.models.flow.actionobjects.serialization.SerializableSparkModel
import com.harana.sdk.shared.models.flow.actionobjects.report.Report
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.AFTSurvivalRegressionModelInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.AFTSurvivalRegressionParameters
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.PredictorParameters
import org.apache.spark.ml.regression.{AFTSurvivalRegression => SparkAFTSurvivalRegression, AFTSurvivalRegressionModel => SparkAFTSurvivalRegressionModel}

class AFTSurvivalRegressionModel
    extends SparkModelWrapper[SparkAFTSurvivalRegressionModel, SparkAFTSurvivalRegression]
    with AFTSurvivalRegressionModelInfo {

  override def report(extended: Boolean = true) = {
    val summary = List(
      SparkSummaryEntry("coefficients", sparkModel.coefficients, "Regression coefficients vector of the beta parameter."),
      SparkSummaryEntry("intercept", sparkModel.intercept, "Intercept of the beta parameter."),
      SparkSummaryEntry("scale", sparkModel.scale, "The log of scale parameter - log(sigma).")
    )
    super.report(extended).withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
  }

  def loadModel(ctx: ExecutionContext, path: String) = new SerializableSparkModel(SparkAFTSurvivalRegressionModel.load(path))
}