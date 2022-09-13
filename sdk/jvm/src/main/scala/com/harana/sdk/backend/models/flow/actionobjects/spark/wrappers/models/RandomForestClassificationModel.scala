package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.backend.models.flow.actionobjects.{LoadableWithFallback, SparkModelWrapper}
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators.SparkSummaryEntry
import com.harana.sdk.backend.models.flow.actionobjects.stringindexingwrapper.StringIndexingWrapperModel
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.{RandomForestClassificationModelInfo, VanillaRandomForestClassificationModelInfo}
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.ProbabilisticClassifierParameters
import com.harana.spark.Linalg
import com.harana.spark.ML
import org.apache.spark.ml.classification.{RandomForestClassificationModel => SparkRandomForestClassificationModel}
import org.apache.spark.ml.classification.{RandomForestClassifier => SparkRandomForestClassifier}
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType

class RandomForestClassificationModel(model: VanillaRandomForestClassificationModel = new VanillaRandomForestClassificationModel())
    extends StringIndexingWrapperModel[SparkRandomForestClassificationModel, SparkRandomForestClassifier](model) with RandomForestClassificationModelInfo {

  def this() = this(new VanillaRandomForestClassificationModel())

}

class VanillaRandomForestClassificationModel
    extends SparkModelWrapper[SparkRandomForestClassificationModel, SparkRandomForestClassifier]
    with VanillaRandomForestClassificationModelInfo
    with LoadableWithFallback[SparkRandomForestClassificationModel, SparkRandomForestClassifier]
    with ProbabilisticClassifierParameters {

  override def applyTransformSchema(schema: StructType) =
    Some(
      StructType(
        schema.fields ++ Seq(
          StructField(getPredictionColumn, DoubleType),
          StructField(getProbabilityColumn, new Linalg.VectorUDT),
          StructField(getRawPredictionColumn, new Linalg.VectorUDT)
        )
      )
    )

  override def report(extended: Boolean = true) = {
    val treeWeight = SparkSummaryEntry("tree weights", sparkModel.treeWeights, "Weights for each tree.")
    super.report(extended).withAdditionalTable(CommonTablesGenerators.modelSummary(List(treeWeight)))
  }

  override def transformerName = classOf[RandomForestClassificationModel].getSimpleName
  def tryToLoadModel(path: String) = ML.ModelLoading.randomForestClassification(path)
}