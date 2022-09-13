package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators.SparkSummaryEntry
import com.harana.sdk.backend.models.flow.actionobjects.stringindexingwrapper.StringIndexingWrapperModel
import com.harana.sdk.backend.models.flow.actionobjects.{LoadableWithFallback, SparkModelWrapper}
import com.harana.sdk.backend.models.flow.utils.Logging
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.{GBTClassificationModelInfo, VanillaGBTClassificationModelInfo}
import com.harana.spark.ML
import org.apache.spark.ml.classification.{GBTClassificationModel => SparkGBTClassificationModel, GBTClassifier => SparkGBTClassifier}
import org.apache.spark.sql.types.{DoubleType, StructField, StructType}

class GBTClassificationModel(model: VanillaGBTClassificationModel)
    extends StringIndexingWrapperModel[SparkGBTClassificationModel, SparkGBTClassifier](model) with GBTClassificationModelInfo {

  def this() = this(new VanillaGBTClassificationModel())
}

class VanillaGBTClassificationModel()
    extends SparkModelWrapper[SparkGBTClassificationModel, SparkGBTClassifier]
    with LoadableWithFallback[SparkGBTClassificationModel, SparkGBTClassifier]
    with VanillaGBTClassificationModelInfo
    with Logging {

  override def applyTransformSchema(schema: StructType) = Some(StructType(schema.fields :+ StructField(getPredictionColumn, DoubleType)))

  override def report(extended: Boolean = true) = {
    val summary = List(SparkSummaryEntry("number of features", sparkModel.numFeatures, "Number of features the model was trained on."))
    super
      .report(extended)
      .withReportName(s"${this.getClass.getSimpleName} with ${sparkModel.getNumTrees} trees")
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
      .withAdditionalTable(CommonTablesGenerators.decisionTree(sparkModel.treeWeights, sparkModel.trees), 2)
  }

  override def transformerName = classOf[GBTClassificationModel].getSimpleName

  def tryToLoadModel(path: String): Option[SparkGBTClassificationModel] = ML.ModelLoading.GBTClassification(path)
}
