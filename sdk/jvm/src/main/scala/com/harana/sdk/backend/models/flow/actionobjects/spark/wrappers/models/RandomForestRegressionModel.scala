package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.backend.models.flow.actionobjects.{LoadableWithFallback, SparkModelWrapper}
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators.SparkSummaryEntry
import com.harana.sdk.shared.models.flow.actionobjects.report.Report
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.RandomForestRegressionModelInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.PredictorParameters
import com.harana.sdk.shared.models.flow.parameters.Parameter
import com.harana.spark.ML
import org.apache.spark.ml.regression.{RandomForestRegressionModel => SparkRFRModel}
import org.apache.spark.ml.regression.{RandomForestRegressor => SparkRFR}

class RandomForestRegressionModel
    extends SparkModelWrapper[SparkRFRModel, SparkRFR]
    with LoadableWithFallback[SparkRFRModel, SparkRFR]
    with RandomForestRegressionModelInfo {

  override def report(extended: Boolean = true) = {
    val summary = List(
      SparkSummaryEntry("number of features", sparkModel.numFeatures, "Number of features the model was trained on."),
      SparkSummaryEntry("feature importances", sparkModel.featureImportances, "Estimate of the importance of each feature.")
    )

    val numTrees = ML.ModelParams.numTreesFromRandomForestRegressionModel(sparkModel)
    super
      .report(extended)
      .withReportName(s"${this.getClass.getSimpleName} with $numTrees trees")
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
      .withAdditionalTable(CommonTablesGenerators.decisionTree(sparkModel.treeWeights, sparkModel.trees), 2)
  }

  def tryToLoadModel(path: String) = ML.ModelLoading.randomForestRegression(path)
}