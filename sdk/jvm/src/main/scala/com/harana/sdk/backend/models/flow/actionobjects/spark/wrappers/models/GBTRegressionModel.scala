package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.backend.models.flow.actionobjects.{LoadableWithFallback, SparkModelWrapper}
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators.SparkSummaryEntry
import com.harana.sdk.shared.models.flow.actionobjects.report.Report
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.GBTRegressionModelInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.PredictorParameters
import com.harana.sdk.shared.models.flow.parameters.Parameter
import com.harana.spark.ML
import org.apache.spark.ml.regression.{GBTRegressionModel => SparkGBTRegressionModel}
import org.apache.spark.ml.regression.{GBTRegressor => SparkGBTRegressor}

class GBTRegressionModel
    extends SparkModelWrapper[SparkGBTRegressionModel, SparkGBTRegressor]
    with LoadableWithFallback[SparkGBTRegressionModel, SparkGBTRegressor]
    with GBTRegressionModelInfo {

  override def report(extended: Boolean = true) = {
    val summary = List(SparkSummaryEntry("number of features", sparkModel.numFeatures, "Number of features the model was trained on."))
    super
      .report(extended)
      .withReportName(s"${this.getClass.getSimpleName} with ${sparkModel.getNumTrees} trees")
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
      .withAdditionalTable(CommonTablesGenerators.decisionTree(sparkModel.treeWeights, sparkModel.trees), 2)
  }

  def tryToLoadModel(path: String): Option[SparkGBTRegressionModel] = ML.ModelLoading.GBTRegression(path)
}
