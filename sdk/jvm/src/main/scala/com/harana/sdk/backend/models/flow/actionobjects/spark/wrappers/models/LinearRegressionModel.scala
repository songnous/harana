package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.SparkModelWrapper
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators.SparkSummaryEntry
import com.harana.sdk.backend.models.flow.actionobjects.serialization.SerializableSparkModel
import com.harana.sdk.shared.models.flow.actionobjects.report.Report
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.LinearRegressionModelInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.PredictorParameters
import com.harana.sdk.shared.models.flow.parameters.Parameter
import org.apache.spark.ml.regression.LinearRegressionTrainingSummary
import org.apache.spark.ml.regression.{LinearRegression => SparkLinearRegression}
import org.apache.spark.ml.regression.{LinearRegressionModel => SparkLinearRegressionModel}

class LinearRegressionModel extends SparkModelWrapper[SparkLinearRegressionModel, SparkLinearRegression]
  with LinearRegressionModelInfo {

  override def report(extended: Boolean = true) = {
    val coefficients = SparkSummaryEntry("coefficients", sparkModel.coefficients, "Weights computed for every feature.")

    val summary = if (sparkModel.hasSummary) {
      val modelSummary: LinearRegressionTrainingSummary = sparkModel.summary
      List(
        SparkSummaryEntry("explained variance", modelSummary.explainedVariance, "Explained variance regression score."),
        SparkSummaryEntry("mean absolute error", modelSummary.meanAbsoluteError, "Mean absolute error is a risk function corresponding to the expected value of the absolute error loss or l1-norm loss."),
        SparkSummaryEntry("mean squared error", modelSummary.meanSquaredError, "Mean squared error is a risk function corresponding to the expected value of the squared error loss or quadratic loss."),
        SparkSummaryEntry("root mean squared error", modelSummary.rootMeanSquaredError, "Root mean squared error is defined as the square root of the mean squared error."),
        SparkSummaryEntry("R^2^", modelSummary.r2, "R^2^ is the coefficient of determination."),
        SparkSummaryEntry("objective history", modelSummary.objectiveHistory, "Objective function (scaled loss + regularization) at each iteration."),
        SparkSummaryEntry("total iterations", modelSummary.totalIterations, "Number of training iterations until termination."),
        SparkSummaryEntry("number of instances", modelSummary.numInstances, "Number of instances in DataFrame predictions."),
        SparkSummaryEntry("deviance residuals", modelSummary.devianceResiduals, "The weighted residuals, the usual residuals rescaled by the square root of the instance weights."),
        SparkSummaryEntry("coefficient standard errors", modelSummary.coefficientStandardErrors, "Standard error of estimated coefficients and intercept."),
        SparkSummaryEntry("t-values", modelSummary.tValues, "T-statistic of estimated coefficients and intercept."),
        SparkSummaryEntry("p-values", modelSummary.pValues, "Two-sided p-value of estimated coefficients and intercept.")
      )
    } else List()

    super
      .report(extended)
      .withAdditionalTable(CommonTablesGenerators.modelSummary(List(coefficients) ++ summary))
  }

  def loadModel(ctx: ExecutionContext, path: String) =
    new SerializableSparkModel(SparkLinearRegressionModel.load(path))
}
