package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.SparkSingleColumnParameterModelWrapper
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators
import com.harana.sdk.backend.models.flow.actionobjects.serialization.SerializableSparkModel
import com.harana.sdk.shared.models.flow.actionobjects.report.Report
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.PCAModelInfo
import com.harana.sdk.shared.models.flow.parameters.Parameter
import com.harana.spark.ML
import org.apache.spark.ml.feature.{PCA => SparkPCA, PCAModel => SparkPCAModel}

import scala.language.reflectiveCalls

class PCAModel extends SparkSingleColumnParameterModelWrapper[SparkPCAModel, SparkPCA] with PCAModelInfo {

  override def report(extended: Boolean = true) =
    super
      .report(extended)
      .withAdditionalTable(
        CommonTablesGenerators.denseMatrix("A Principal Components Matrix", "Each column is one principal component.", ML.ModelParams.pcFromPCAModel(sparkModel))
      )

  def loadModel(ctx: ExecutionContext, path: String) = new SerializableSparkModel(SparkPCAModel.load(path))
}