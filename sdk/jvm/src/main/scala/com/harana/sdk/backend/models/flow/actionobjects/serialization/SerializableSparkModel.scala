package com.harana.sdk.backend.models.flow.actionobjects.serialization

import com.harana.spark.ML
import org.apache.spark.ml.Model
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.ml.util.MLReadable
import org.apache.spark.ml.util.MLReader
import org.apache.spark.ml.util.MLWritable
import org.apache.spark.ml.util.MLWriter
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.StructType

class SerializableSparkModel[M <: Model[M]](val sparkModel: M) extends ML.Model[SerializableSparkModel[M]] with MLWritable {
  val uid = "dc7178fe-b209-44f5-8a74-d3c4dafa0fae"

  def copy(extra: ParamMap) = new SerializableSparkModel(sparkModel.copy(extra))

  def write = sparkModel match {
      case w: MLWritable => w.write
      case _ => new DefaultMLWriter(this)
    }

  def transformDF(dataset: DataFrame) = sparkModel.transform(dataset)
  def transformSchema(schema: StructType) = sparkModel.transformSchema(schema)
}

object SerializableSparkModel extends MLReadable[SerializableSparkModel[_]] {
  def read = new DefaultMLReader[SerializableSparkModel[_]]()
}
