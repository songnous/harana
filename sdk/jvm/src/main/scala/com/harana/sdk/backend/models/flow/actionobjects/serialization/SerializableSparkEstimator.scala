package com.harana.sdk.backend.models.flow.actionobjects.serialization

import com.harana.spark.ML
import org.apache.spark.ml.Estimator
import org.apache.spark.ml.Model
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.ml.util.MLWritable
import org.apache.spark.ml.util.MLWriter
import org.apache.spark.sql
import org.apache.spark.sql.types.StructType

class SerializableSparkEstimator[T <: Model[T], E <: Estimator[T]](val sparkEstimator: E) extends ML.Estimator[SerializableSparkModel[T]] with MLWritable {
  val uid = "e2a121fe-da6e-4ef2-9c5e-56ee558c14f0"

  def fitDF(dataset: sql.DataFrame) = new SerializableSparkModel[T](sparkEstimator.fit(dataset))
  def copy(extra: ParamMap) = new SerializableSparkEstimator[T, E](sparkEstimator.copy(extra).asInstanceOf[E])
  def write = new DefaultMLWriter(this)
  def transformSchema(schema: StructType) = sparkEstimator.transformSchema(schema)

}