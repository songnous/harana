package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.backend.models.flow.actionobjects.{LoadableWithFallback, SparkModelWrapper}
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators.SparkSummaryEntry
import com.harana.sdk.shared.models.flow.actionobjects.report.Report
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.MultilayerPerceptronClassifierModelInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.PredictorParameters
import com.harana.sdk.shared.models.flow.parameters.Parameter
import com.harana.spark.ML
import org.apache.spark.ml.classification.{MultilayerPerceptronClassificationModel => SparkMultilayerPerceptronClassifierModel}
import org.apache.spark.ml.classification.{MultilayerPerceptronClassifier => SparkMultilayerPerceptronClassifier}

class MultilayerPerceptronClassifierModel
    extends SparkModelWrapper[SparkMultilayerPerceptronClassifierModel, SparkMultilayerPerceptronClassifier]
    with LoadableWithFallback[SparkMultilayerPerceptronClassifierModel, SparkMultilayerPerceptronClassifier]
    with MultilayerPerceptronClassifierModelInfo {

  override def report(extended: Boolean = true) = {
    val summary = List(
      SparkSummaryEntry("number of features", sparkModel.numFeatures, "Number of features."),
      SparkSummaryEntry("layers", sparkModel.layers, "The list of layer sizes that includes the input layer size as the first number and the output layer size as the last number."),
      SparkSummaryEntry("weights", sparkModel.weights, "The vector of perceptron layers' weights.")
    )

    super
      .report(extended)
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
  }

  def tryToLoadModel(path: String) = ML.ModelLoading.multilayerPerceptronClassification(path)
}
