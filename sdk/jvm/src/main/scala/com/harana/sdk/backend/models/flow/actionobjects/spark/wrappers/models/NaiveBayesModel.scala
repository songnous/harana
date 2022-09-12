package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.SparkModelWrapper
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators.SparkSummaryEntry
import com.harana.sdk.backend.models.flow.actionobjects.serialization.SerializableSparkModel
import com.harana.sdk.shared.models.flow.actionobjects.report.Report
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.NaiveBayesModelInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.ProbabilisticClassifierParameters
import com.harana.sdk.shared.models.flow.parameters.Parameter
import org.apache.spark.ml.classification.{NaiveBayes => SparkNaiveBayes}
import org.apache.spark.ml.classification.{NaiveBayesModel => SparkNaiveBayesModel}

class NaiveBayesModel extends SparkModelWrapper[SparkNaiveBayesModel, SparkNaiveBayes]
  with NaiveBayesModelInfo {

  override def report(extended: Boolean = true) = {
    val summary = List(
      SparkSummaryEntry("pi", sparkModel.pi, "Log of class priors, whose dimension is C (number of classes)"),
      SparkSummaryEntry("theta", sparkModel.theta, "Log of class conditional probabilities, whose dimension is C (number of classes) by D (number of features)")
    )

    super
      .report(extended)
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
  }

  def loadModel(ctx: ExecutionContext, path: String) = new SerializableSparkModel(SparkNaiveBayesModel.load(path))
}