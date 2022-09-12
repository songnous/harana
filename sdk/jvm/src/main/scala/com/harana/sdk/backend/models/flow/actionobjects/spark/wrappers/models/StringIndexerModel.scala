package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators.SparkSummaryEntry
import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.{MultiColumnModel, SparkSingleColumnParameterModelWrapper, Transformer}
import com.harana.sdk.backend.models.flow.actionobjects.report.CommonTablesGenerators
import com.harana.sdk.backend.models.flow.actionobjects.serialization.{JsonObjectPersistence, SerializableSparkModel}
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.{MultiColumnStringIndexerModelInfo, SingleColumnStringIndexerModelParameterInfo}
import io.circe.syntax._
import org.apache.hadoop.fs.Path
import org.apache.spark.ml.feature.{StringIndexer => SparkStringIndexer, StringIndexerModel => SparkStringIndexerModel}

trait StringIndexerModel extends Transformer

class MultiColumnStringIndexerModel
  extends MultiColumnModel[SparkStringIndexerModel, SparkStringIndexer, SingleColumnStringIndexerModelParameter]
  with StringIndexerModel
  with MultiColumnStringIndexerModelInfo {

  override def report(extended: Boolean = true) = {
    val tables = models.map(model => model.report(extended).content.tables)
    val name = s"${this.getClass.getSimpleName} with ${models.length} columns"
    tables.foldRight(super.report(extended).withReportName(name))((seqTables, accReport) =>
      seqTables.foldRight(accReport)((t, r) => r.withAdditionalTable(t))
    )
  }

  override def loadTransformer(ctx: ExecutionContext, path: String) = this.setModels(loadModels(ctx, path))
  override def saveTransformer(ctx: ExecutionContext, path: String) = saveModels(ctx, path)

  private def saveModels(ctx: ExecutionContext, path: String) = {
    saveNumberOfModels(ctx, path, models.size)
    val modelsPath = Transformer.modelFilePath(path)
    models.zipWithIndex.foreach { case (model, index) => model.save(ctx, new Path(modelsPath, index.toString).toString)}
  }

  private def saveNumberOfModels(ctx: ExecutionContext, path: String, numberOfModels: Int) = {
    val json = (MultiColumnStringIndexerModel.numberOfModelsKey -> numberOfModels.toString).asJson
    val numberOfModelsFilePath = MultiColumnStringIndexerModel.numberOfModelsPath(path)
    JsonObjectPersistence.saveJsonToFile(ctx, numberOfModelsFilePath, json)
  }

  private def loadModels(ctx: ExecutionContext, path: String) = {
    val numberOfModels = loadNumberOfModels(ctx, path)
    val modelsPath = Transformer.modelFilePath(path)
    (0 until numberOfModels).map { index =>
      Transformer.load(ctx, new Path(modelsPath, index.toString).toString).asInstanceOf[SingleColumnStringIndexerModelParameter]
    }
  }

  private def loadNumberOfModels(ctx: ExecutionContext, path: String): Int = {
    val numberOfModelsFilePath = MultiColumnStringIndexerModel.numberOfModelsPath(path)
    val json = JsonObjectPersistence.loadJsonFromFile(ctx, numberOfModelsFilePath)
    json.hcursor.downField(MultiColumnStringIndexerModel.numberOfModelsKey).as[Int].toOption.get
  }

  def loadModel(ctx: ExecutionContext, path: String) =
    throw new UnsupportedOperationException("There is no single model to load for MultiColumnStringIndexerModel")
}

object MultiColumnStringIndexerModel {
  val numberOfModelsKey = "numberOfModels"
  val numberOfModelsFileName = "numberOfModels"
  def numberOfModelsPath(path: String) = new Path(path, numberOfModelsFileName).toString
}

class SingleColumnStringIndexerModelParameter
  extends SparkSingleColumnParameterModelWrapper[SparkStringIndexerModel, SparkStringIndexer]
    with StringIndexerModel
    with SingleColumnStringIndexerModelParameterInfo {

  override def report(extended: Boolean = true) = {
    val summary = List(SparkSummaryEntry("labels", sparkModel.labelsArray.flatten, "Ordered list of labels, corresponding to indices to be assigned."))
    super.report(extended).withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
  }

  def loadModel(ctx: ExecutionContext, path: String) = new SerializableSparkModel(SparkStringIndexerModel.load(path))
}