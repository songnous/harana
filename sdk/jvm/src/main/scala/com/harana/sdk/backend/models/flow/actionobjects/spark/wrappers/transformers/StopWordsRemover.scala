package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.sdk.backend.models.flow.actionobjects.SparkTransformerAsMultiColumnTransformer
import com.harana.sdk.backend.models.flow.inference.exceptions.TransformSchemaError
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers.StopWordsRemoverInfo
import org.apache.spark.ml.feature.{StopWordsRemover => SparkStopWordsRemover}
import org.apache.spark.sql.types.StructType

class StopWordsRemover extends SparkTransformerAsMultiColumnTransformer[SparkStopWordsRemover] with StopWordsRemoverInfo {

  override def transformSingleColumnSchema(inputColumn: String, outputColumn: String, schema: StructType): Option[StructType] = {
    try {
      val inputFields = schema.fieldNames
      require(!inputFields.contains(outputColumn), s"Output column $outputColumn already exists.")
    } catch {
      case e: Exception => throw TransformSchemaError(e.getMessage, Some(e)).toException
    }
    super.transformSingleColumnSchema(inputColumn, outputColumn, schema)
  }
}