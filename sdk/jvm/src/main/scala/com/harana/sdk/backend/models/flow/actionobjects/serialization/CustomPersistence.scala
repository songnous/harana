package com.harana.sdk.backend.models.flow.actionobjects.serialization

import com.harana.sdk.backend.models.flow.utils.{Logging, Serialization}
import org.apache.spark.SparkContext
object CustomPersistence extends Logging {

  def save[T](sparkContext: SparkContext, instance: T, path: String) = {
    val data = Serialization.serialize(instance)
    val rdd = sparkContext.parallelize(data.toIndexedSeq, 1)
    rdd.saveAsTextFile(path)
  }

  def load[T](sparkContext: SparkContext, path: String): T = {
    println("Reading objects from: {}", path)
    val rdd = sparkContext.textFile(path)
    val data = rdd.map(_.toByte).collect()
    Serialization.deserialize(data)
  }
}