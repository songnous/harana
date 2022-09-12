package com.harana.sdk.backend.models.flow.actionobjects.stringindexingwrapper

import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actionobjects.serialization.{DefaultMLReader, DefaultMLWriter}
import com.harana.sdk.shared.models.flow.parameters.Parameters
import com.harana.spark.ML
import org.apache.spark.annotation.DeveloperApi
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.IndexToString
import org.apache.spark.ml.feature.StringIndexer
import org.apache.spark.ml.util._
import org.apache.spark.ml
import org.apache.spark.ml.param.Params
import org.apache.spark.sql
import org.apache.spark.sql.types.DataType
import org.apache.spark.sql.types.StructType

import java.util.UUID

/** In order to add string-indexing behaviour to estimators we need to put it into Sparks Pipeline */
object StringIndexingPipeline {

  def apply[M, T](
      dataFrame: DataFrame,
      sparkEstimator: ML.Estimator[_],
      labelColumnName: String,
      predictionColumnName: String
  ): Pipeline = {

    val sparkDataFrame = dataFrame.sparkDataFrame

    val indexedLabelColumnName = UUID.randomUUID().toString
    val stringIndexer          = new StringIndexer()
      .setInputCol(labelColumnName)
      .setOutputCol(indexedLabelColumnName)
      .fit(sparkDataFrame)

    val predictedLabelsColumnName = UUID.randomUUID().toString
    val labelConverter = new IndexToString()
      .setInputCol(predictionColumnName)
      .setOutputCol(predictedLabelsColumnName)
      .setLabels(stringIndexer.labelsArray.flatten)

    val tempLabelsColumnName = UUID.randomUUID().toString
    val predictionColumnType = sparkDataFrame.schema(labelColumnName).dataType
    new Pipeline().setStages(
      Array(
        stringIndexer,
        new RenameColumnTransformer(labelColumnName, tempLabelsColumnName),
        new RenameColumnTransformer(indexedLabelColumnName, labelColumnName),
        sparkEstimator,
        labelConverter,
        new FilterNotTransformer(Set(labelColumnName)),
        new RenameColumnTransformer(tempLabelsColumnName, labelColumnName),
        new SetUpPredictionColumnTransformer(predictionColumnName, predictionColumnType, predictedLabelsColumnName)
      )
    )
  }
}

class RenameColumnTransformer(private val originalColumnName: String, private val newColumnName: String)
    extends ML.Transformer
    with MLWritable
    with Params {

  def transformDF(dataset: sql.DataFrame): sql.DataFrame = {
    // WARN: cannot use dataset.withColumnRenamed - it does not preserve metadata.
    val fieldsNames = dataset.schema.fieldNames
    val columns     = fieldsNames.map { name =>
      if (name == originalColumnName)
        dataset(name).as(newColumnName)
      else
        dataset(name)
    }
    dataset.select(columns.toIndexedSeq: _*)
  }

  def copy(extra: ml.param.ParamMap): ml.Transformer =
    new RenameColumnTransformer(originalColumnName, newColumnName)

  @DeveloperApi
  def transformSchema(schema: StructType) = StructType(schema.fields.map { field => if (field.name == originalColumnName) field.copy(name = newColumnName) else field})

  val uid = Identifiable.randomUID("RenameColumnTransformer")

  def write: MLWriter = new DefaultMLWriter(this)
}

object RenameColumnTransformer extends MLReadable[RenameColumnTransformer] {
  def read = new DefaultMLReader[RenameColumnTransformer]()
}

class FilterNotTransformer(private val columnsToOmit: Set[String]) extends ML.Transformer with MLWritable {

  def transformDF(dataset: sql.DataFrame): sql.DataFrame = {
    val fieldsNames = dataset.schema.fieldNames.filterNot(columnsToOmit.contains)
    val columns     = fieldsNames.map(dataset(_)).toIndexedSeq
    dataset.select(columns: _*)
  }

  def copy(extra: ml.param.ParamMap) = new FilterNotTransformer(columnsToOmit)

  @DeveloperApi
  def transformSchema(schema: StructType) = StructType(schema.fields.filterNot(field => columnsToOmit.contains(field.name)))

  val uid = Identifiable.randomUID("FilterNotTransformer")

  def write: MLWriter = new DefaultMLWriter(this)

}

object FilterNotTransformer extends MLReadable[FilterNotTransformer] {
  def read = new DefaultMLReader[FilterNotTransformer]()
}

class SetUpPredictionColumnTransformer(predictionColumnName: String, predictionColumnType: DataType, predictedLabelsColumnName: String) extends ML.Transformer with MLWritable {
  import org.apache.spark.sql.functions._

  private val outSet = Set(predictedLabelsColumnName, predictionColumnName)

  def transformDF(dataset: sql.DataFrame) = {
    val columnsNames         = dataset.schema.fieldNames.filterNot(outSet.contains)
    val predictionColumnType = dataset.schema(predictionColumnName).dataType
    val cols                 = columnsNames.map(col) :+ col(predictedLabelsColumnName).as(predictionColumnName).cast(predictionColumnType)
    dataset.select(cols.toIndexedSeq: _*)
  }

  def copy(extra: ml.param.ParamMap) = new SetUpPredictionColumnTransformer(predictionColumnName, predictionColumnType, predictedLabelsColumnName)

  @DeveloperApi
  def transformSchema(schema: StructType) = {
    val columns = schema.fields.filterNot(field => outSet.contains(field.name)) :+ schema(predictedLabelsColumnName).copy(name = predictionColumnName, dataType = predictionColumnType)
    StructType(columns)
  }

  val uid = Identifiable.randomUID("SetUpPredictionColumnTransformer")
  def write: MLWriter = new DefaultMLWriter(this)
}

object SetUpPredictionColumnTransformer extends MLReadable[SetUpPredictionColumnTransformer] {
  def read = new DefaultMLReader[SetUpPredictionColumnTransformer]()
}
