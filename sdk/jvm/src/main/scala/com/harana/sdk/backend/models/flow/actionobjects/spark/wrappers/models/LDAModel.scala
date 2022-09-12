package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators.SparkSummaryEntry
import com.harana.sdk.backend.models.flow.actionobjects.SparkModelWrapper
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators
import com.harana.sdk.backend.models.flow.actionobjects.serialization.SerializableSparkModel
import com.harana.sdk.backend.models.flow.utils.Logging
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.LDAModelInfo
import org.apache.spark.ml.clustering.{DistributedLDAModel, LocalLDAModel, LDA => SparkLDA, LDAModel => SparkLDAModel}

class LDAModel extends SparkModelWrapper[SparkLDAModel, SparkLDA] with LDAModelInfo with Logging {

  override def report(extended: Boolean = true) = {
    val vocabularySize = SparkSummaryEntry("vocabulary size", sparkModel.vocabSize, "The number of terms in the vocabulary.")
    val estimatedDocConcentration = SparkSummaryEntry("estimated doc concentration", sparkModel.estimatedDocConcentration, "Value for `doc concentration` estimated from data.")
    super
      .report(extended)
      .withAdditionalTable(CommonTablesGenerators.modelSummary(List(vocabularySize, estimatedDocConcentration)))
  }

  def loadModel(ctx: ExecutionContext, path: String) =
    try
      new SerializableSparkModel(LocalLDAModel.load(path))
    catch {
      case e: IllegalArgumentException =>
        logger.warn(s"LocalLDAModel.load($path) failed. Trying to load DistributedLDAModel.", e)
        new SerializableSparkModel(DistributedLDAModel.load(path))
    }

}