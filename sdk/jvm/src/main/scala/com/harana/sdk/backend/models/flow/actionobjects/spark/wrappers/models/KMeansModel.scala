package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.SparkModelWrapper
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators.SparkSummaryEntry
import com.harana.sdk.backend.models.flow.actionobjects.serialization.SerializableSparkModel
import com.harana.sdk.shared.models.flow.actionobjects.report.Report
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.KMeansModelInfo
import com.harana.sdk.shared.models.flow.parameters.Parameter
import org.apache.spark.ml.clustering.{KMeans => SparkKMeans}
import org.apache.spark.ml.clustering.{KMeansModel => SparkKMeansModel}

class KMeansModel extends SparkModelWrapper[SparkKMeansModel, SparkKMeans] with KMeansModelInfo {

  override def report(extended: Boolean = true) = {
    val summary = List(SparkSummaryEntry("cluster centers", sparkModel.clusterCenters, "Positions of cluster centers."))
    super
      .report(extended)
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
  }

  def loadModel(ctx: ExecutionContext, path: String) =
    new SerializableSparkModel(SparkKMeansModel.load(path))
}
